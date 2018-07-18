package com.example.passportdemo.sort;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 可重入锁加条件
 *
 * @author muxiaorui
 * @create 2018-07-18 16:08
 * ReentrantLock和Synchronized
 * Synchronized是Lock的一种简化实现，一个Lock可以对应多个
 * Condition，而synchronized把Lock和Condition合并了，一个
 * synchronized Lock只对应一个Condition，可以说Synchronized是
 * Lock的简化版本。
 * 在JDK5，Synchronized要比Lock慢很多，但是在JDK6中，它们的
 * 效率差不多
 * 不要在Lock和Condition上使用wait、notiffy、notifyAll方法！
 * await -> wait
 * singal -> notify
 * singalAll-> notifyAll
 **/
public class BlockingQ2 {
    private Lock lock=new ReentrantLock();
    private Condition notEmpty=lock.newCondition();
    private Condition notFull=lock.newCondition();
    private Queue<Object> linkedList=new LinkedList<Object>();
    private int maxLength=10;
    public Object take()throws InterruptedException{
        lock.lock();
        try{
            if(linkedList.size()==0){
                notEmpty.await();
            }
            if(linkedList.size()==maxLength){
                notFull.signalAll();
            }
            return  linkedList.poll();
        }finally {
            lock.unlock();
        }
    }

    public void offer(Object object)throws InterruptedException{
        lock.lock();
        try{
            if(linkedList.size()==0){
                notEmpty.signalAll();
            }
            if(linkedList.size()==maxLength){
                notFull.await();
            }
            linkedList.add(object);
        }finally {
            lock.unlock();
        }
    }
}
