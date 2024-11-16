package com.ticketPing.order.infrastructure.listener;

import com.ticketPing.order.application.service.OrderService;
import events.PaymentCompletedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mapper.EventSerializer;
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