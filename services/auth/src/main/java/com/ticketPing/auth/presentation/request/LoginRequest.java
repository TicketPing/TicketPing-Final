package com.ticketPing.auth.presentation.request;

import com.ticketPing.auth.exception.LoginErrorMessage;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record LoginRequest(
        @NotBlank(message = LoginErrorMessage.EMAIL_REQUIRED)
        @Pattern(regexp = "^[A-Za-z0-9]+@[A-Za-z0-9]+\\.[A-Za-z]{2,6}",
                message = LoginErrorMessage.INVALID_EMAIL)
        String email,

        @NotBlank(message = LoginErrorMessage.PASSWORD_REQUIRED)
        String password
) { }
