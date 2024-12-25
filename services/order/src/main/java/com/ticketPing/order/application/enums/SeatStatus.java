package com.ticketPing.order.application.enums;

import com.ticketPing.order.common.exception.OrderExceptionCase;
import exception.ApplicationException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum SeatStatus {
    AVAILABLE("AVAILABLE"),
    HELD("HELD"),
    RESERVED("RESERVED");

    private final String value;

    public static SeatStatus getSeatStatus(final String value) {
        return Arrays.stream(SeatStatus.values())
        .filter(t -> t.getValue().equals(value))
        .findAny().orElseThrow(() -> new ApplicationException(OrderExceptionCase.INVALID_SEAT_STATUS));
        }
}
