package com.example.passportdemo;

import com.example.passportdemo.jms.Producer;
import org.apache.activemq.command.ActiveMQQueue;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.jms.Destination;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PassportdemoApplicationTests {


	@Autowired
	private Producer producer;
	@Test
	public void contextLoads() throws Exception {
		Destination destination = new ActiveMQQueue("passport.queue");

		for(int i=0; i<100; i++){
			producer.send(destination, "发送"+(i+1)+"消息："+"activityMQ");
			System.out.println("发送"+(i+1)+"消息："+"activityMQ");
			Thread.sleep(300);
		}
	}

}
