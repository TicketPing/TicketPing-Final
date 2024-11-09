package com.ticketPing.queue_manage.infrastructure.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RedisKeyPrefix {

    WAITING_QUEUE("{Queue}:WaitingQueue:"),
    WORKING_QUEUE("{Queue}:WorkingQueue:"),
    TOKEN_VALUE("{Queue}:UserToken:"),
    LEADER_KEY("{Queue}:LeaderKey:");

    private final String value;

}
