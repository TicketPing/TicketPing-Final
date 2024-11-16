package caching.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RedisKeyPrefix {

    WAITING_QUEUE("{Queue}:WaitingQueue:"),
    WORKING_QUEUE("{Queue}:WorkingQueue:"),
    TOKEN_VALUE("{Queue}:UserToken:");

    private final String value;

}
