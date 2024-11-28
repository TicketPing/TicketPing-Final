package com.ticketPing.auth.application.service.enums;

import com.ticketPing.auth.common.exception.AuthErrorCase;
import exception.ApplicationException;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Role {
    USER("USER"),
    COMPANY("COMPANY");

    private final String value;

    public static Role getRole(final String value) {
        try {
            return Role.valueOf(value);
        } catch (final IllegalArgumentException e) {
            throw new ApplicationException(AuthErrorCase.INVALID_ROLE);
        }
    }
}
