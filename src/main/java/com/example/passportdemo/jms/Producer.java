package com.example.passportdemo.jms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.stereotype.Service;

import javax.jms.Destination;

/**
 * activemq producer
 *
 * @author muxiaorui
 * @create 2018-05-21 11:45
 **/

@Service("producer")
public class Producer {
    @Autowired
    private JmsMessagingTemplate jmsTemplate;


    public void send(Destination destination, final String message){
        this.jmsTemplate.convertAndSend(destination,message);
    }
}
