package com.ticketPing.gateway.common.exception;

import exception.ErrorCase;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum SecurityErrorCase implements ErrorCase {

    USER_CACHE_IS_NULL(HttpStatus.INTERNAL_SERVER_ERROR, "내부 서버 오류");

    private final HttpStatus httpStatus;
    private final String message;

}
