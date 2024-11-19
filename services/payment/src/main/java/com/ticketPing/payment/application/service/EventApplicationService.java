package com.ticketPing.payment.application.service;

import messaging.events.PaymentCompletedEvent;
import lombok.RequiredArgsConstructor;
import messaging.events.PaymentCreatedEvent;
import messaging.utils.EventLogger;
import messaging.utils.EventSerializer;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import messaging.utils.EventSerializer;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import messaging.events.PaymentCompletedEvent;
import messaging.topics.PaymentTopic;

@Service
@RequiredArgsConstructor
public class EventApplicationService {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void publishPaymentCreatedEvent(PaymentCreatedEvent event) {
        publishEvent(PaymentTopic.CREATED.getTopic(), event);
    }

    public void publishPaymentCompletedEvent(PaymentCompletedEvent event) {
        publishEvent(PaymentTopic.COMPLETED.getTopic(), event);
    }

    private <T> void publishEvent(String topic, T event) {
        String message = EventSerializer.serialize(event);
        kafkaTemplate.send(topic, message);
        EventLogger.logSentMessage(topic, message);
    }

}