package com.example.passportdemo.redisLimiter;

import com.google.common.util.concurrent.RateLimiter;

/**
 * Created by songjian on 7/3/2018.
 */
public class HaoqiaoRateLimter {
    public static void main(String[] args) throws Exception{
        RedisLimiter rt1 = RedisLimiter.create(1d,"HAO_QIAO");
        RateLimiter rt = RateLimiter.create(1d);
        while (true){
            System.out.println(rt.acquire());
//            Thread.sleep(508);
        }
//        System.out.println(2 * TimeUnit.MINUTES.toSeconds(1) );
    }
}
