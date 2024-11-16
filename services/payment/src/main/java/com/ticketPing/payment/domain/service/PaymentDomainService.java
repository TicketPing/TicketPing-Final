package com.ticketPing.payment.domain.service;

import static com.ticketPing.payment.common.exception.PaymentErrorCase.PAYMENT_NOT_FOUND;

import com.ticketPing.payment.domain.model.entity.Payment;
import com.ticketPing.payment.domain.repository.PaymentRepository;
import exception.ApplicationException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import order.OrderInfoForPaymentResponse;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentDomainService {

    private final PaymentRepository paymentRepository;

    public Payment createPayment(UUID userId, OrderInfoForPaymentResponse orderInfo) {
        Payment payment = Payment.create(userId, orderInfo);
        paymentRepository.save(payment);
        return payment;
    }

    public Payment completePayment(UUID paymentId) {
        Payment payment = findPayment(paymentId);
        payment.complete();
        return payment;
    }

    public Payment findPayment(UUID paymentId) {
        return paymentRepository.findById(paymentId)
                .orElseThrow(() -> new ApplicationException(PAYMENT_NOT_FOUND));
    }

}