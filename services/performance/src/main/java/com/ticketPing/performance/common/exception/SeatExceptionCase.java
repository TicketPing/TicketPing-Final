package com.ticketPing.performance.common.exception;

import exception.ErrorCase;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum SeatExceptionCase implements ErrorCase {
    SEAT_NOT_FOUND(HttpStatus.BAD_REQUEST, "좌석 정보를 찾을 수 없습니다."),
    SEAT_CACHE_NOT_FOUND(HttpStatus.BAD_REQUEST, "좌석 캐싱 정보를 찾을 수 없습니다."),
    INVALID_SEAT_STATUA(HttpStatus.BAD_REQUEST, "유효하지 않은 좌석 상태입니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
