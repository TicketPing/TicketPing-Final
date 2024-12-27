package com.ticketPing.performance;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.ticketPing.performance", "aop", "exception", "audit"})
public class PerformanceApplication {
    public static void main(String[] args) {
        SpringApplication.run(PerformanceApplication.class, args);
    }
}
