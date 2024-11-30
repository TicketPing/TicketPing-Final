package com.ticketPing.payment.application.dto;

import com.ticketPing.payment.domain.model.entity.Payment;
import java.time.LocalDateTime;
import mapper.ObjectMapperBasedVoMapper;
import java.util.UUID;

public record PaymentResponse(
        UUID Id,
        UUID userId,
        String status,
        UUID orderId,
        Long amount,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static PaymentResponse from(Payment payment) {
        return ObjectMapperBasedVoMapper.convert(payment, PaymentResponse.class);
    }
}
