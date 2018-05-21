package com.example.passportdemo.jms;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

/**
 * Consumer
 *
 * @author muxiaorui
 * @create 2018-05-21 13:45
 **/
@Component
public class Consumer {


    // 使用JmsListener配置消费者监听的队列，其中text是接收到的消息
    @JmsListener(destination = "passport.queue")
    public void receiveQueue(String text) {
        System.out.println("Consumer收到的报文为:"+text);
    }
}
