package com.ticketPing.queue_manage.application.service;

import com.ticketPing.queue_manage.application.dto.GeneralQueueTokenResponse;
import com.ticketPing.queue_manage.domain.command.waitingQueue.FindWaitingQueueTokenCommand;
import com.ticketPing.queue_manage.domain.command.waitingQueue.InsertWaitingQueueTokenCommand;
import com.ticketPing.queue_manage.domain.command.workingQueue.FindWorkingQueueTokenCommand;
import com.ticketPing.queue_manage.domain.repository.WaitingQueueRepository;
import com.ticketPing.queue_manage.domain.repository.WorkingQueueRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class WaitingQueueService {

    private final WaitingQueueRepository waitingQueueRepository;
    private final WorkingQueueRepository workingQueueRepository;

    public Mono<GeneralQueueTokenResponse> enterWaitingQueue(String userId, String performanceId) {
        val command = InsertWaitingQueueTokenCommand.create(userId, performanceId);
        return waitingQueueRepository.insertWaitingQueueToken(command)
                .doOnSuccess(token -> log.info("대기열 진입 완료 {}", token))
                .map(GeneralQueueTokenResponse::from);
    }

    public Mono<GeneralQueueTokenResponse> getQueueInfo(String userId, String performanceId) {
        val command = FindWaitingQueueTokenCommand.create(userId, performanceId);
        return waitingQueueRepository.findWaitingQueueToken(command)
                .doOnSuccess(token -> log.info("대기열 토큰 조회 완료 {}", token))
                .map(GeneralQueueTokenResponse::from)
                .switchIfEmpty(findWorkingQueueToken(userId, performanceId));
    }

    private Mono<GeneralQueueTokenResponse> findWorkingQueueToken(String userId, String performanceId) {
        val command = FindWorkingQueueTokenCommand.create(userId, performanceId);
        return workingQueueRepository.findWorkingQueueToken(command)
                .doOnSuccess(token -> log.info("작업열 토큰 조회 완료 {}", token))
                .map(GeneralQueueTokenResponse::from);
    }

}