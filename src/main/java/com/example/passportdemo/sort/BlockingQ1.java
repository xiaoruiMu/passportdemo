package com.example.passportdemo.sort;

import java.util.LinkedList;
import java.util.Queue;

/**
 * 阻塞队列2
 *
 * @author muxiaorui
 * @create 2018-07-18 16:03
 **/
public class BlockingQ1 {
    private Object notEmpty=new Object();
    private Object notFull=new Object();
    private Queue<Object> linkedList=new LinkedList<Object>();
    private int maxLength=10;
    //获取
    public Object take() throws InterruptedException{
        //wait() notify(),notifyall()都必须使用在同步中，
        // 因为要对持有监视器（锁）的线程操作
        //wait()暂停的是持有锁的对象 notify 唤醒的是等待锁的对象
        synchronized (notEmpty){
            if(linkedList.size()==0){
                notEmpty.wait();
            }
            synchronized (notFull){
                if(linkedList.size()==maxLength){
                    notFull.notifyAll();
                }
                return linkedList.poll();
            }

        }
    }
    //添加
    public void offer(Object object) throws InterruptedException{
        synchronized (notEmpty){
            if(linkedList.size()==0){
                notEmpty.notifyAll();
            }
            synchronized (notFull){
                if(linkedList.size()==maxLength){
                    notFull.wait();
                }
                linkedList.add(object);
            }

        }

    }
}
