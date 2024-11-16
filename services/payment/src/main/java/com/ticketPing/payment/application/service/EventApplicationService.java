package com.ticketPing.payment.application.service;

import lombok.RequiredArgsConstructor;
import mapper.EventSerializer;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import events.PaymentCompletedEvent;
import topics.PaymentTopic;

@Service
@RequiredArgsConstructor
public class EventApplicationService {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void publishPaymentCompletedEvent(PaymentCompletedEvent event) {
        kafkaTemplate.send(PaymentTopic.COMPLETED.getTopic(), EventSerializer.serialize(event));
    }

}