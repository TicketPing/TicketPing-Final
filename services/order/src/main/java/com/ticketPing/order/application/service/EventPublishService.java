package com.ticketPing.order.application.service;

import common.utils.EventSerializer;
import events.OrderCompletedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import topics.OrderTopic;

@Service
@RequiredArgsConstructor
public class EventPublishService {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void publishOrderCompletedEvent(OrderCompletedEvent event) {
        kafkaTemplate.send(OrderTopic.COMPLETED.getTopic(), EventSerializer.serialize(event));
    }

}
