package com.example.passportdemo;


import com.example.passportdemo.filter.DemoFilter;
import org.apache.activemq.command.ActiveMQQueue;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
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
	@Bean
	public FilterRegistrationBean filterDemo4Registration(){
		FilterRegistrationBean registration = new FilterRegistrationBean();
		//注入过滤器
		registration.setFilter(new DemoFilter());
		//拦截规则
		registration.addUrlPatterns("/*");
		//过滤器名称
		registration.setName("demoFilter");
		//是否自动注册 false 取消Filter的自动注册
		registration.setEnabled(false);
		//过滤器顺序
		registration.setOrder(1);
		return registration;
	}

}
