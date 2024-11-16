package com.ticketPing.queue_manage.common.exception;

import cases.ErrorCase;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum QueueErrorCase implements ErrorCase {

    USER_TOKEN_NOT_FOUND(HttpStatus.NOT_FOUND ,"대기열에 존재하지 않는 사용자입니다."),
    WORKING_QUEUE_TOKEN_NOT_FOUND(HttpStatus.NOT_FOUND ,"작업열에 존재하지 않는 사용자입니다.");

    private final HttpStatus httpStatus;
    private final String message;

}

