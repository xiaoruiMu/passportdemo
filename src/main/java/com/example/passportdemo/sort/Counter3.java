package com.example.passportdemo.sort;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Lock Free 算法
 *
 * @author muxiaorui
 * @create 2018-07-19 10:19
 * 通常都是三个部分组成
 * ① 循环
 * ② CAS (CompareAndSet
 * ③ 回退
 **/
public class Counter3 {
    private AtomicInteger max=new AtomicInteger();
    public void set(int value){
        for(;;){
          int current=max.get();
          if(max.compareAndSet(current,value)){
              break;
          }else{
              continue;
          }
        }
    }

    public int getMax(){
        return max.get();
    }
}
