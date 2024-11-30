package com.ticketPing.order.infrastructure.config;

import messaging.topics.OrderTopic;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    @Bean
    public NewTopic orderCompletedTopic() {
        return TopicBuilder.name(OrderTopic.COMPLETED.getTopic())
                .partitions(3)
                .replicas(3)
                .build();
    }

}