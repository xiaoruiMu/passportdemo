package com.example.passportdemo.sort;

/**
 * 计数器
 *
 * @author muxiaorui
 * @create 2018-07-19 10:14
 **/
public class Counter2 {
    private volatile int max=0;
    public synchronized void set (int value){
        if(value>max){
         max=value;
        }
    }
    public int getMax(){
        return max;
    }
}
