package com.example.passportdemo.sort;

/**
 * 计数器
 *
 * @author muxiaorui
 * @create 2018-07-18 16:39
 **/
public class Counter {
    private volatile int count=0;
    public synchronized void increment(){
        count++;
    }
    public int getCount(){
        return count;
    }
}
