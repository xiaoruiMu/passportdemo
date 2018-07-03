package com.example.passportdemo.serial;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * test
 *
 * @author muxiaorui
 * @create 2018-06-25 11:01
 **/
public class PersonTest {
    public static void main(String args[]) throws  Exception{
        System.out.println("test 开始");
        ObjectOutputStream out = new ObjectOutputStream(
                new FileOutputStream("man"));
        Man t1=new Man("F","我是男孩");
        t1.setName("王");
        t1.setAge(12);
        out.writeObject(new Man("F","我是男孩"));
        out.close();
        System.out.println("输入数据："+t1.toString());
        ObjectInputStream oin = new ObjectInputStream(new FileInputStream(
                "man"));
        Man t = (Man) oin.readObject();

        oin.close();

        //再读取，通过t.staticVar打印新的值
        System.out.println("返回结果："+t.getName());
        System.out.println("返回结果："+t.getAge());
        System.out.println("返回结果："+t.toString());
    }
}
