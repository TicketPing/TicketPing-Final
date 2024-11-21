package caching.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RedisKeyPrefix {

    // 대기열
    WAITING_QUEUE("{Queue}:WaitingQueue:"),
    WORKING_QUEUE("{Queue}:WorkingQueue:"),
    TOKEN_VALUE("{Queue}:UserToken:"),

    // 공연
    AVAILABLE_SEATS("AvailableSeats:"),
    SEAT_CACHE("{Seat}:seat:");

    private final String value;

}
