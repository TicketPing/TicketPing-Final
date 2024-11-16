package com.ticketPing.queue_manage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages = {"com.ticketPing.queue_manage", "exception", "caching.config"})
@SpringBootApplication
public class QueueManageApplication {
	public static void main(String[] args) {
		SpringApplication.run(QueueManageApplication.class, args);
	}
}
