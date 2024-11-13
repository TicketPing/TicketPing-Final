package com.ticketPing.payment.application.service;

import static com.ticketPing.payment.domain.exception.PaymentErrorCase.PAYMENT_NOT_FOUND;

import com.ticketPing.payment.application.dto.PaymentResponse;
import com.ticketPing.payment.domain.model.entity.Payment;
import com.ticketPing.payment.domain.repository.PaymentRepository;
import com.ticketPing.payment.infrastructure.client.OrderClient;
import exception.ApplicationException;
import events.PaymentCompletedEvent;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import order.OrderInfoResponse;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderClient orderClient;
    private final EventService eventPublishService;

    @Transactional
    public PaymentResponse requestPayment(UUID userId, UUID orderId) {
        OrderInfoResponse orderInfo = orderClient.getOrderInfo(orderId);
        // TODO: PG사 결제 요청
        Payment payment = createPayment(userId, orderInfo);
        return PaymentResponse.from(payment);
    }

    private Payment createPayment(UUID userId, OrderInfoResponse orderInfo) {
        Payment payment = Payment.create(userId, orderInfo);
        paymentRepository.save(payment);
        return payment;
    }

    @Transactional
    public PaymentResponse checkPaymentStatus(UUID paymentId) {
        // TODO: PG사 결제 상태 확인
        Payment payment = findPayment(paymentId);
        payment.complete();
        eventPublishService.publishPaymentCompletedEvent(PaymentCompletedEvent.create(paymentId));
        return PaymentResponse.from(payment);
    }

    private Payment findPayment(UUID paymentId) {
        return paymentRepository.findById(paymentId)
                .orElseThrow(() -> new ApplicationException(PAYMENT_NOT_FOUND));
    }

}

