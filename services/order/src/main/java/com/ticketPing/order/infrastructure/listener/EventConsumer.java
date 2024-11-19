package com.ticketPing.order.infrastructure.listener;

import com.ticketPing.order.application.service.OrderService;
import messaging.events.PaymentCompletedEvent;
import messaging.events.PaymentCreatedEvent;
import lombok.RequiredArgsConstructor;
import messaging.utils.EventLogger;
import messaging.utils.EventSerializer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EventConsumer {

    private final OrderService orderService;

    @KafkaListener(topics = "payment-created", groupId = "order-group")
    public void handlePaymentCreatedEvent(ConsumerRecord<String, String> record) {
        EventLogger.logReceivedMessage(record);
        PaymentCreatedEvent event = EventSerializer.deserialize(record.value(), PaymentCreatedEvent.class);
        // TODO Order 엔티티 paymentId 주입
    }

    @KafkaListener(topics = "payment-completed", groupId = "order-group")
    public void handlePaymentCompletedEvent(ConsumerRecord<String, String> record) {
        EventLogger.logReceivedMessage(record);
        PaymentCompletedEvent event = EventSerializer.deserialize(record.value(), PaymentCompletedEvent.class);
        orderService.updateOrderStatus(event.orderId());
    }

}