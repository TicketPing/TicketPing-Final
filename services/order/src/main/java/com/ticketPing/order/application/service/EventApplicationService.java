package com.ticketPing.order.application.service;

import messaging.utils.EventSerializer;
import messaging.events.OrderCompletedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import messaging.topics.OrderTopic;

@Service
@RequiredArgsConstructor
public class EventApplicationService {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public void publishOrderCompletedEvent(OrderCompletedEvent event) {
        String message = EventSerializer.serialize(event);
        kafkaTemplate.send(OrderTopic.COMPLETED.getTopic(), message);
    }

}
