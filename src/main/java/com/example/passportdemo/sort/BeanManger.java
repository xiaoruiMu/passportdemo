package com.example.passportdemo.sort;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * BeanManger
 *
 * @author muxiaorui
 * @create 2018-07-19 10:27
 * ConcurrentHashMap并没有实现Lock-Free，只是使用了分离锁的办
 * 法使得能够支持多个Writer并发。 ConcurrentHashMap需要使用更
 * 多的内存
 **/
public class BeanManger {
    private Map<String, Object> map = new HashMap<String, Object>();

    public Object getBean(String key) {
        synchronized (map) {//锁了整个map
            Object bean = map.get(key);
            if (bean == null) {
                map.put(key, createBean());
                bean = map.get(key);
            }
            return bean;
        }
    }
   public Object createBean(){
        return new Object();
    }


    private ConcurrentMap<String, Object> map1 = new ConcurrentHashMap<String, Object>();
//使用ConcurrentMap，避免直接使用锁，锁由数据结构来管理
    public Object getBean1(String key) {
        Object bean = map1.get(key);
        if (bean == null) {
            map1.putIfAbsent(key, createBean());
            bean = map1.get(key);
        }
        return bean;
    }
}
