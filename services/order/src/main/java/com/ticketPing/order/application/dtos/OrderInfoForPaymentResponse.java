package com.ticketPing.order.application.dtos;

import com.ticketPing.order.domain.model.entity.Order;
import java.util.UUID;
import mapper.ObjectMapperBasedVoMapper;

public record OrderInfoForPaymentResponse(
        UUID id,
        UUID performanceId,
        UUID performanceScheduleId,
        UUID seatId,
        Long amount
) {
    public static OrderInfoForPaymentResponse from(Order order) {
        return ObjectMapperBasedVoMapper.convert(order, OrderInfoForPaymentResponse.class);
    }
}
