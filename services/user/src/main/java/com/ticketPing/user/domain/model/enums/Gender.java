package com.ticketPing.user.domain.model.enums;

import com.ticketPing.user.common.exception.UserErrorCase;
import exception.ApplicationException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum Gender {
    MALE("MALE"),
    FEMALE("FEMALE");

    private final String value;

    public static Gender getGender(final String value) {
        return Arrays.stream(Gender.values())
                .filter(t -> t.getValue().equals(value))
                .findAny().orElseThrow(() -> new ApplicationException(UserErrorCase.INVALID_GENDER));
    }
}