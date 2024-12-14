package com.ticketPing.queue_manage.infrastructure.repository;

import com.ticketPing.queue_manage.domain.command.waitingQueue.DeleteFirstWaitingQueueTokenCommand;
import com.ticketPing.queue_manage.domain.command.waitingQueue.FindWaitingQueueTokenCommand;
import com.ticketPing.queue_manage.domain.command.waitingQueue.InsertWaitingQueueTokenCommand;
import com.ticketPing.queue_manage.domain.model.QueueToken;
import com.ticketPing.queue_manage.domain.model.WaitingQueueToken;
import com.ticketPing.queue_manage.domain.model.WorkingQueueToken;
import com.ticketPing.queue_manage.domain.model.enums.TokenStatus;
import com.ticketPing.queue_manage.domain.repository.WaitingQueueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
public class WaitingQueueRepositoryImpl implements WaitingQueueRepository {

    private final LuaScriptRepository luaScriptRepository;

    @Override
    public Mono<QueueToken> insertWaitingQueueToken(InsertWaitingQueueTokenCommand command) {
        return luaScriptRepository.saveToken(command)
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
        return luaScriptRepository.getRankAndSize(command)
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
        return luaScriptRepository.deleteFirstToken(command)
                .map(tokenValue -> WaitingQueueToken.valueOf(command.getPerformanceId(), tokenValue));
    }

}