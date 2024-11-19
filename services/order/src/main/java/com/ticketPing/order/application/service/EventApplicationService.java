package com.ticketPing.order.application.service;

import messaging.utils.EventLogger;
import messaging.utils.EventSerializer;
import messaging.events.OrderCompletedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import messaging.topics.OrderTopic;

@Service
@RequiredArgsConstructor
public class EventApplicationService {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void publishOrderCompletedEvent(OrderCompletedEvent event) {
        publishEvent(OrderTopic.COMPLETED.getTopic(), event);
    }

    private <T> void publishEvent(String topic, T event) {
        String message = EventSerializer.serialize(event);
        kafkaTemplate.send(topic, message);
        EventLogger.logSentMessage(topic, message);
    }

}
