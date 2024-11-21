package com.ticketPing.auth.common.exception;

import exception.ErrorCase;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum AuthErrorCase implements ErrorCase {
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 사용자입니다."),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 로그인 토큰입니다."),
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "로그인이 만료되었습니다."),
    UNSUPPORTED_AUTHENTICATION(HttpStatus.UNAUTHORIZED, "지원되지 않는 로그인 토큰입니다."),
    WRONG_TYPE_TOKEN(HttpStatus.UNAUTHORIZED, "로그인 토큰 타입이 맞지 않습니다."),
    INVALID_ROLE(HttpStatus.UNAUTHORIZED, "유효하지 않은 사용자 역할입니다."),
    INVALID_USER_ID(HttpStatus.UNAUTHORIZED, "유효하지 않은 userId 입니다."),
    REFRESH_TOKEN_NOT_FOUND(HttpStatus.UNAUTHORIZED, "리프레시 토큰이 존재하지 않습니다. 다시 로그인해주세요.");

    private final HttpStatus httpStatus;
    private final String message;
}
