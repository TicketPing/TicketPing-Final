package com.ticketPing.payment.application.service;

import com.ticketPing.payment.application.dto.PaymentResponse;
import com.ticketPing.payment.domain.model.entity.Payment;
import com.ticketPing.payment.domain.service.PaymentDomainService;
import com.ticketPing.payment.infrastructure.client.OrderClient;
import messaging.events.PaymentCompletedEvent;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.val;
import messaging.events.PaymentCreatedEvent;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentApplicationService {

    private final PaymentDomainService paymentDomainService;
    private final OrderClient orderClient;
    private final EventApplicationService eventApplicationService;

    @Transactional
    public PaymentResponse requestPayment(UUID userId, UUID orderId) {
        val orderInfo = orderClient.getOrderInfo(orderId, userId);

        // TODO: PG사 결제 요청

        Payment payment = paymentDomainService.createPayment(userId, orderInfo);

        publishPaymentCreatedEvent(payment);

        return PaymentResponse.from(payment);
    }

    private void publishPaymentCreatedEvent(Payment payment) {
        val event = PaymentCreatedEvent.create(payment.getId(), payment.getOrderId());
        eventApplicationService.publishPaymentCreatedEvent(event);
    }

    @Transactional
    public PaymentResponse checkPaymentStatus(UUID paymentId) {
        // TODO: PG사 결제 상태 확인

        Payment payment = paymentDomainService.completePayment(paymentId);

        publishPaymentCompletedEvent(payment);
        return PaymentResponse.from(payment);
    }

    private void publishPaymentCompletedEvent(Payment payment) {
        val event = PaymentCompletedEvent.create(payment.getOrderId());
        eventApplicationService.publishPaymentCompletedEvent(event);
    }

}

