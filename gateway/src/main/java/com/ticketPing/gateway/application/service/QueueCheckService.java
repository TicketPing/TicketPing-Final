package com.ticketPing.gateway.application.service;

import static caching.enums.RedisKeyPrefix.AVAILABLE_SEATS;
import static caching.enums.RedisKeyPrefix.WAITING_QUEUE;
import static com.ticketPing.gateway.common.utils.QueueTokenValueGenerator.generateTokenValue;

import caching.repository.RedisRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QueueCheckService {

    private final RedisRepository redisRepository;

    // 매진 여부
    public boolean checkPerformanceSoldOut(String performanceId) {
        return getAvailableSeatsCount(performanceId) <= 0;
    }

    // 공연 잔여석 * 2 <= 대기열 인원 수
    public boolean checkTooManyWaitingUsers(String performanceId) {
        return getAvailableSeatsCount(performanceId) * 2 <= getWaitingUsersCount(performanceId);
    }

    // 작업열 토큰 조회
    public boolean checkUserAvailable(String userId, String performanceId) {
        return checkTokenExists(userId, performanceId);
    }

    public long getAvailableSeatsCount(String performanceId) {
        String key = AVAILABLE_SEATS.getValue() + performanceId;
        return (long) redisRepository.getValue(key);
    }

    private long getWaitingUsersCount(String performanceId) {
        String key = WAITING_QUEUE.getValue() + performanceId;
        return redisRepository.getSortedSetSize(key);
    }

    private boolean checkTokenExists(String userId, String performanceId) {
        String tokenValue = generateTokenValue(userId, performanceId);
        return redisRepository.hasKey(tokenValue);
    }

}