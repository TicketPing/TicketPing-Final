package com.ticketPing.queue_manage.domain.command.workingQueue;

import static com.ticketPing.queue_manage.infrastructure.enums.RedisKeyPrefix.WORKING_QUEUE;

import com.ticketPing.queue_manage.domain.model.enums.WorkingQueueTokenDeleteCase;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

/**
 * queueName: 작업열 이름 (작업 인원 카운터 키)
 * tokenValue: 사용자 토큰 값 (작업열 토큰 키)
 */
@Getter
@Builder(access = AccessLevel.PRIVATE)
public class DeleteWorkingQueueTokenCommand {

    private String queueName;
    private String tokenValue;
    private WorkingQueueTokenDeleteCase deleteCase;

    public static DeleteWorkingQueueTokenCommand create(WorkingQueueTokenDeleteCase deleteCase, String tokenValue) {
        String performanceId = tokenValue.split(":")[2];
        return DeleteWorkingQueueTokenCommand.builder()
                .queueName(WORKING_QUEUE.getValue() + performanceId)
                .tokenValue(tokenValue)
                .deleteCase(deleteCase)
                .build();
    }

}
