package com.ticketPing.queue_manage.infrastructure.repository;

import com.ticketPing.queue_manage.domain.command.waitingQueue.DeleteFirstWaitingQueueTokenCommand;
import com.ticketPing.queue_manage.domain.command.waitingQueue.FindWaitingQueueTokenCommand;
import com.ticketPing.queue_manage.domain.command.waitingQueue.InsertWaitingQueueTokenCommand;
import com.ticketPing.queue_manage.domain.model.QueueToken;
import com.ticketPing.queue_manage.domain.model.WaitingQueueToken;
import com.ticketPing.queue_manage.domain.model.WorkingQueueToken;
import com.ticketPing.queue_manage.domain.model.enums.TokenStatus;
import com.ticketPing.queue_manage.domain.repository.WaitingQueueRepository;
import com.ticketPing.queue_manage.infrastructure.repository.script.SaveTokenScript;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
public class WaitingQueueRepositoryImpl implements WaitingQueueRepository {

    private final RedisRepository redisRepository;
    private final SaveTokenScript script;

    @Override
    public Mono<QueueToken> insertWaitingQueueToken(InsertWaitingQueueTokenCommand command) {
        return script.saveToken(command)
                .flatMap(tokenStatus -> createToken(
                        tokenStatus,
                        command.getUserId(),
                        command.getPerformanceId()
                ));
    }

    private Mono<QueueToken> createToken(TokenStatus tokenStatus, String userId, String performanceId) {
        return tokenStatus == TokenStatus.WORKING
                ? Mono.just(WorkingQueueToken.create(userId, performanceId))
                : Mono.just(WaitingQueueToken.create(userId, performanceId));
    }

    @Override
    public Mono<WaitingQueueToken> findWaitingQueueToken(FindWaitingQueueTokenCommand command) {
        return redisRepository.getSortedSet(command.getQueueName())
                .flatMap(ss -> Mono.zip(
                        ss.rank(command.getTokenValue()),
                        ss.size()
                ))
                .map(tuple -> WaitingQueueToken.withPosition(
                        command.getUserId(),
                        command.getPerformanceId(),
                        command.getTokenValue(),
                        tuple.getT1() + 1,
                        tuple.getT2()
                ));
    }

    @Override
    public Mono<WaitingQueueToken> deleteFirstWaitingQueueToken(DeleteFirstWaitingQueueTokenCommand command) {
        return redisRepository.getSortedSet(command.getQueueName())
                .flatMap(ss -> ss.first()
                        .delayUntil(ss::remove)
                )
                .map(tokenValue -> WaitingQueueToken.valueOf(command.getPerformanceId(), tokenValue));
    }

}