package com.ticketPing.auth.common.exception;

import exception.ErrorCase;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum AuthErrorCase implements ErrorCase {
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 사용자입니다."),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다."),
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "토큰이 만료되었습니다."),
    ACCESS_TOKEN_NOT_EXPIRED(HttpStatus.UNAUTHORIZED, "엑세스 토큰이 만료되지 않았습니다."),
    INVALID_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "리프레시 토큰이 일치하지 않습니다."),
    REFRESH_TOKEN_NOT_FOUND(HttpStatus.UNAUTHORIZED, "리프레시 토큰이 만료되었습니다. 다시 로그인해주세요."),
    REFRESH_TOKEN_NOT_PROVIDED(HttpStatus.UNAUTHORIZED, "쿠키에 리프레시 토큰이 존재하지 않습니다."),
    INVALID_ROLE(HttpStatus.UNAUTHORIZED, "유효하지 않은 사용자 역할입니다."),
    INVALID_USER_ID(HttpStatus.UNAUTHORIZED, "유효하지 않은 userId 입니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
