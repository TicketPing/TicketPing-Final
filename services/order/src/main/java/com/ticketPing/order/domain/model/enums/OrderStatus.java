package com.ticketPing.order.domain.model.enums;

import lombok.Getter;

@Getter
public enum OrderStatus {

    PENDING("결제 대기"),
    FAIL("예매 실패"),
    COMPLETED("예매 완료"),
    CANCELED("예매 취소");

    private final String value;

    OrderStatus(String value) {
        this.value = value;
    }
}
