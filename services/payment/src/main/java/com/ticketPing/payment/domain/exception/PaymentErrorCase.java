package com.ticketPing.payment.domain.exception;

import cases.ErrorCase;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum PaymentErrorCase implements ErrorCase {

    PAYMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "결제 정보가 존재하지 않습니다.");

    private final HttpStatus httpStatus;
    private final String message;

}
