package com.ticketPing.user.presentation.cases;

import cases.SuccessCase;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum UserSuccessCase implements SuccessCase {
    SUCCESS_CREATE_USER(HttpStatus.CREATED, "User created"),
    SUCCESS_GET_USER(HttpStatus.OK, "Success get user"),;

    private final HttpStatus httpStatus;
    private final String message;
}
