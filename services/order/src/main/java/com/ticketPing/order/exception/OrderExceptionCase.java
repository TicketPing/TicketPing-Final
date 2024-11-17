package com.ticketPing.order.exception;


import cases.ErrorCase;
import exception.ApplicationException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum OrderExceptionCase implements ErrorCase {

    ORDER_NOT_FOUND(HttpStatus.NOT_FOUND, "예매정보를 찾을 수 없습니다."),
    INVALID_TTL_NAME(HttpStatus.BAD_REQUEST, "유효하지 않은 TTL 명입니다."),
    NOT_FOUND_ORDER_ID_IN_TTL(HttpStatus.NOT_FOUND,"TTL 정보에서 order_id를 찾을 수 없습니다."),
    SEAT_ALREADY_TAKEN(HttpStatus.BAD_REQUEST, "좌석이 이미 점유되어 있습니다."),
    SEAT_CACHE_NOT_FOUND(HttpStatus.NOT_FOUND, "레디스에 공연관련 정보가 캐싱되어 있지 않습니다."),
    ORDER_STATUS_UNKNOWN(HttpStatus.CONFLICT,"저장된 enum 상태값을 사용해야 합니다."),
    PRE_RESERVE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "좌석 선점 과정에서 오류가 발생했습니다."),
    SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 오류가 발생했습니다.");

    private final HttpStatus httpStatus;
    private final String message;

    public static OrderExceptionCase getByValue(String value) {
        try {
            return OrderExceptionCase.valueOf(value);
        } catch (IllegalArgumentException e) {
            return OrderExceptionCase.SERVER_ERROR;
        }
    }
}
