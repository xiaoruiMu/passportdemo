package com.example.passportdemo;


import org.apache.activemq.command.ActiveMQQueue;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

import javax.jms.Queue;


@SpringBootApplication
@MapperScan("com.example.passportdemo.mapper")
public class PassportdemoApplication {
//	@Bean
//	public Queue queue() {
//		return new ActiveMQQueue("passport.queue");
//	}

	public static void main(String[] args) {
		ConfigurableApplicationContext ctx=SpringApplication.run(PassportdemoApplication.class, args);
		System.out.println("server.port: " + (ctx.getEnvironment().getProperty("server.port")) );
		System.out.println("author.zhName: " + (ctx.getEnvironment().getProperty("author.zhName")) );
	}


}
