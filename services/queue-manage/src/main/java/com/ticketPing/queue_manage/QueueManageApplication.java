package com.ticketPing.queue_manage;

import common.aop.LoggingAspect;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.FilterType;

@EnableFeignClients
@ComponentScan(basePackages = {"com.ticketPing.queue_manage", "common"},
		excludeFilters = @Filter(type = FilterType.ASSIGNABLE_TYPE, value = LoggingAspect.class))
@SpringBootApplication
public class QueueManageApplication {
	public static void main(String[] args) {
		SpringApplication.run(QueueManageApplication.class, args);
	}
}
