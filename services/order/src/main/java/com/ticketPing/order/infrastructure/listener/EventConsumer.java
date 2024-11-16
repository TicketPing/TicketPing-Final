package com.ticketPing.order.infrastructure.listener;

import com.ticketPing.order.application.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import messaging.events.PaymentCompletedEvent;
import messaging.utils.EventSerializer;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class EventConsumer {

    private final OrderService orderService;

    @KafkaListener(topics = "payment-completed", groupId = "order-group")
    public void handlePaymentCompletedEvent(String message) {
        log.info("Received message from kafka: {}", message);
        PaymentCompletedEvent event = EventSerializer.deserialize(message, PaymentCompletedEvent.class);
        orderService.updateOrderStatus(event.orderId());
    }

}