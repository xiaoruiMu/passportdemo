package com.example.passportdemo.sort;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Counter
 *
 * @author muxiaorui
 * @create 2018-07-18 16:45
 **/
public class Counter1 {
    private AtomicInteger count=new AtomicInteger();
    public void increment(){
        count.incrementAndGet();
    }
    public int getCount(){
       return count.get();
    }
}
