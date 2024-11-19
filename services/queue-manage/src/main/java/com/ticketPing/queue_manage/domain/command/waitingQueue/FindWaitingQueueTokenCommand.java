package com.ticketPing.queue_manage.domain.command.waitingQueue;

import static caching.enums.RedisKeyPrefix.WAITING_QUEUE;
import static com.ticketPing.queue_manage.common.utils.TokenValueGenerator.generateTokenValue;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

/**
 * queueName: 대기열 이름 (대기열 Sorted Set 키)
 * tokenValue: 사용자 토큰 값 (대기열 Sorted Set 멤버)
 */
@Getter
@Builder(access = AccessLevel.PRIVATE)
public class FindWaitingQueueTokenCommand {

    private String userId;
    private String performanceId;
    private String queueName;
    private String tokenValue;

    public static FindWaitingQueueTokenCommand create(String userId, String performanceId) {
        return FindWaitingQueueTokenCommand.builder()
                .userId(userId)
                .performanceId(performanceId)
                .queueName(WAITING_QUEUE.getValue() + performanceId)
                .tokenValue(generateTokenValue(userId, performanceId))
                .build();
    }

}
