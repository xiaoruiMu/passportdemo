package com.example.passportdemo.redisLimiter;

import com.google.common.util.concurrent.Uninterruptibles;
import org.apache.commons.lang.StringUtils;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * Created by songjian on 7/4/2018.
 */
public class RedisLimiter {
    private double storedPermits;
    private double maxPermits;
    private double stableIntervalMicros;
    private long nextFreeTicketMicros;
    private double maxBurstSeconds;

    private String permitsKey;
    private String timerKey;
    private String lockKey;

    public static JedisCluster jedisCluster;


    public RedisLimiter(Double maxBurstSeconds,Double permitsPerSecond,String baseKey) throws Exception {
        initKye(baseKey);
        //加上分布式锁
        String identifier = lockWithTimeout(lockKey,30,15);
        setRate(permitsPerSecond);
        synToRedis();
        //释放锁
        releaseLock(lockKey,identifier);
    }
    /**
     *获取redis的微秒时间
     */
    private static long readReidsMicro() {
        List<String> list = (List<String>) RedisLimiter.getRedisCulster().eval("return redis.call('time')","timeGetter");
        String str1 = list.get(0);
        String str2 = StringUtils.leftPad(list.get(1),6,"0");
        return Long.parseLong(str1+str2);
    }

    private void setRate(double permitsPerSecond){
        this.resync(this.readReidsMicro());
        double stableIntervalMicros = (double) TimeUnit.SECONDS.toMicros(1L) / permitsPerSecond;
        this.stableIntervalMicros = stableIntervalMicros;
        this.doSetRate(permitsPerSecond, stableIntervalMicros);
    }

    private void doSetRate(double permitsPerSecond, double stableIntervalMicros) {
        double oldMaxPermits = this.maxPermits;
        this.maxPermits = this.maxBurstSeconds * permitsPerSecond;
        this.storedPermits = oldMaxPermits == 0.0D?0.0D:this.storedPermits * this.maxPermits / oldMaxPermits;
    }

    private void resync(long redisMicros){
        initValues(maxBurstSeconds);
        if(redisMicros > this.nextFreeTicketMicros) {
            this.storedPermits = Math.min(this.maxPermits, this.storedPermits + (double)(redisMicros - this.nextFreeTicketMicros) / this.stableIntervalMicros);
            this.nextFreeTicketMicros = redisMicros;
        }
    }
    private void synToRedis(){
        RedisLimiter.getRedisCulster().set(permitsKey,String.valueOf(storedPermits));
        RedisLimiter.getRedisCulster().set(timerKey,String.valueOf(nextFreeTicketMicros));
    }

    /**
     * 获取令牌
     * @return
     */
    public double acquire() {
        return this.acquire(1);
    }
    public double acquire(int permits) {
        long microsToWait;
        //加上分布式锁
        String identifier = lockWithTimeout(lockKey,30,15);
        microsToWait = this.reserveNextTicket((double)permits, this.readReidsMicro());
        synToRedis();

        //释放锁
        releaseLock(lockKey,identifier);
        this.sleepMicrosUninterruptibly(microsToWait);

        return 1.0D * (double)microsToWait / (double)TimeUnit.SECONDS.toMicros(1L);
    }
    private long reserveNextTicket(double requiredPermits, long redisMicros) {
        this.resync(redisMicros);
        long microsToNextFreeTicket = this.nextFreeTicketMicros - redisMicros;
        double storedPermitsToSpend = Math.min(requiredPermits, this.storedPermits);
        double freshPermits = requiredPermits - storedPermitsToSpend;
        long waitMicros =  (long)(freshPermits * this.stableIntervalMicros);
        this.nextFreeTicketMicros += waitMicros;
        this.storedPermits -= storedPermitsToSpend;
        return microsToNextFreeTicket;
    }

    public void sleepMicrosUninterruptibly(long micros) {
        if(micros > 0L) {
            Uninterruptibles.sleepUninterruptibly(micros, TimeUnit.MICROSECONDS);
        }

    }

    /**
     * 初始化值。
     * @param maxBurstSeconds
     */
    private void initValues(Double maxBurstSeconds){
        this.maxBurstSeconds =maxBurstSeconds;
        Boolean keyExist =  RedisLimiter.getRedisCulster().exists(permitsKey);
        if(!keyExist){
            //如果key不存在初始化相应数据
            this.nextFreeTicketMicros = 0L;
        }else{
            //如果key存在加载相应数据
            String permits = RedisLimiter.getRedisCulster().get(permitsKey);
            this.storedPermits = Double.parseDouble(permits);
            String timer = RedisLimiter.getRedisCulster().get(timerKey);
            this.nextFreeTicketMicros = Long.parseLong(timer);
        }
    }

    public String lockWithTimeout(String lockKey,
                                  long acquireTimeout, long timeout) {
        String identifier = UUID.randomUUID().toString();
        String result = null;
        try {
            // 超时时间，上锁后超过此时间则自动释放锁
            int lockExpire = (int)(timeout / 1000);
            // 获取锁的超时时间，超过这个时间则放弃获取锁
            long end = System.currentTimeMillis() + acquireTimeout;
            while (System.currentTimeMillis() < end) {
                if (RedisLimiter.getRedisCulster().setnx(lockKey, identifier) == 1) {
                    RedisLimiter.getRedisCulster().expire(lockKey, lockExpire);
                    // 返回value值，用于释放锁时间确认
                    result = identifier;
                    return result;
                }
                // 返回-1代表key没有设置超时时间，为key设置一个超时时间
                if (RedisLimiter.getRedisCulster().ttl(lockKey) == -1) {
                    RedisLimiter.getRedisCulster().expire(lockKey, lockExpire);
                }

                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public boolean releaseLock(String lockKey, String identifier) {
        boolean result = false;
        try {
            while (true) {
                // 通过前面返回的value值判断是不是该锁，若是该锁，则删除，释放锁
                if (identifier.equals(RedisLimiter.getRedisCulster().get(lockKey))) {
                    RedisLimiter.getRedisCulster().del(lockKey);
                    result = true;
                    return result;
                }
                break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 初始化key
     * @param baseKey
     */
    private void  initKye(String baseKey){
        permitsKey = baseKey + "PERMITS";
        timerKey=baseKey+"TIMER";
        lockKey =baseKey  + "LIMIT_LOCK";
    }

    public static RedisLimiter create(Double permitsPerSecond,String baseKey) throws Exception {
        return new RedisLimiter(1.0D,permitsPerSecond,baseKey);
    }


    public static void main(String[] args) {

//        List<String> strings = (List) jedis.eval("return redis.call('time')");
//        System.out.println(strings.get(0)+strings.get(1));
//        System.out.println(System.currentTimeMillis());
//        Long duration = Long.parseLong(strings.get(0)+strings.get(1))/1000;
//        System.out.println(duration+"");
//        Date date = new Date(duration);
//        System.out.println(date);
//        System.out.println(jedis.set("lll","dds"));
        while (true){
//            List<String> list = (List<String>) RedisLimiter.getRedisCulster().eval("return redis.call('time')","timeGetter");
//            String str1 = list.get(0);
//            String str2 = StringUtils.leftPad(list.get(1),6,"0");
//            System.out.println(str1+"|"+list.get(1));
//            System.out.println(str1+"|"+str2);
            System.out.println(readReidsMicro());
        }



//        System.out.println(RedisLimiter.getRedisCulster().eval("return redis.call('time')","timeGetter"));
//        System.out.println(RedisLimiter.getRedisCulster().eval("return redis.call('time')","timeGetter"));
//        System.out.println(RedisLimiter.getRedisCulster().eval("return redis.call('time')","timeGetter"));
//        System.out.println(RedisLimiter.getRedisCulster().eval("return redis.call('time')","timeGetter"));
//        System.out.println(RedisLimiter.getRedisCulster().eval("return redis.call('time')","timeGetter"));
//        System.out.println(RedisLimiter.getRedisCulster().eval("return redis.call('time')","timeGetter"));
    }

    public static JedisCluster getRedisCulster(){
        if( jedisCluster==null){
            Set<HostAndPort> jedisClusterNodes = new HashSet<HostAndPort>();
            jedisClusterNodes.add(new HostAndPort("10.200.4.74", 6379));
            jedisClusterNodes.add(new HostAndPort("10.200.4.75", 6379));
            jedisClusterNodes.add(new HostAndPort("10.200.4.76", 6379));
            jedisCluster = new JedisCluster(jedisClusterNodes);
            return new JedisCluster(jedisClusterNodes);
        }
            return  jedisCluster;
    }

}
