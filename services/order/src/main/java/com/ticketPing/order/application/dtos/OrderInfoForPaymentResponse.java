package com.ticketPing.order.application.dtos;

import com.ticketPing.order.domain.model.entity.Order;
import java.util.UUID;

public record OrderInfoForPaymentResponse(
        UUID id,
        Long amount
) {
    public static OrderInfoForPaymentResponse from(Order order) {
        return new OrderInfoForPaymentResponse(order.getId(), 5000L);
    }
}
