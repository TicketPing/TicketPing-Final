package com.ticketPing.payment.application.service;

import lombok.RequiredArgsConstructor;
import messaging.utils.EventSerializer;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import messaging.events.PaymentCompletedEvent;
import messaging.topics.PaymentTopic;

@Service
@RequiredArgsConstructor
public class EventApplicationService {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void publishPaymentCompletedEvent(PaymentCompletedEvent event) {
        kafkaTemplate.send(PaymentTopic.COMPLETED.getTopic(), EventSerializer.serialize(event));
    }

}