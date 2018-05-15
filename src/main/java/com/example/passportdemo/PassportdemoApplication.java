package com.example.passportdemo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;


@SpringBootApplication
@MapperScan("com.example.passportdemo.mapper")
public class PassportdemoApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext ctx=SpringApplication.run(PassportdemoApplication.class, args);
		System.out.println("server.port: " + (ctx.getEnvironment().getProperty("server.port")) );
		System.out.println("author.zhName: " + (ctx.getEnvironment().getProperty("author.zhName")) );
	}


}
