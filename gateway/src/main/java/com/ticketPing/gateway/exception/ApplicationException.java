package com.ticketPing.gateway.exception;

import com.ticketPing.gateway.presentation.cases.ErrorCase;
import lombok.Getter;

@Getter
public class ApplicationException extends RuntimeException {
    private final ErrorCase errorCase;

    public ApplicationException(ErrorCase errorCase) {
        super(errorCase.getMessage());
        this.errorCase = errorCase;
    }
}
