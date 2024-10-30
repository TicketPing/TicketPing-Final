package com.ticketPing.queue_manage.infrastructure.repository.script;

import com.ticketPing.queue_manage.domain.command.waitingQueue.InsertWaitingQueueTokenCommand;
import com.ticketPing.queue_manage.domain.model.enums.TokenStatus;
import java.util.Arrays;
import org.redisson.api.RScript;

import java.util.List;
import org.redisson.api.RedissonReactiveClient;
import org.redisson.client.codec.StringCodec;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * tokenValue: 사용자 토큰 값 (대기열 Sorted Set 멤버 or 작업열 토큰 키)
 * waitingQueueName: 대기열 이름 (대기열 Sorted Set 키)
 * enterTime: 대기열 진입 시간 (Sorted Set 스코어)
 * workingQueueName: 작업열 이름 (작업 인원 카운터 키)
 * cacheValue: 작업열 토큰 value
 * ttl: 작업열 토큰 TTL
 * workingQueueMaxSize: 작업열 최대 크기
 */
@Component
public class SaveTokenScript {

    private final RedissonReactiveClient redissonClient;
    private final String scriptSha;

    private static final String SCRIPT =
            // 변수 선언
            "local waitingQueueName = KEYS[1] " +
                    "local workingQueueName = KEYS[2] " +
                    "local workingQueueTokenKey = KEYS[3] " +
                    "local maxSlots = tonumber(ARGV[1]) " +
                    "local tokenValue = ARGV[2] " +
                    "local enterTime = ARGV[3] " +
                    "local cacheValue = ARGV[4] " +
                    "local ttl = tonumber(ARGV[5]) " +
                    // 작업열 여유 인원 조회
                    "local currentWorkers = tonumber(redis.call('GET', workingQueueName) or 0) " +
                    "local availableSlots = maxSlots - currentWorkers " +
                    // 작업열 저장
                    "if availableSlots > 0 then " +
                    "    if redis.call('EXISTS', workingQueueTokenKey) == 0 then " +
                    "       redis.call('SET', workingQueueTokenKey, cacheValue, 'EX', ttl) " +
                    "       redis.call('INCR', workingQueueName) " +
                    "    end " +
                    "    return 1 " +
                    // 대기열 저장
                    "else " +
                    "    if redis.call('EXISTS', workingQueueTokenKey) == 0 then " +
                    "       redis.call('ZADD', waitingQueueName, enterTime, tokenValue) " +
                    "       return 0 " +
                    "    end " +
                    "       return 1 " +
                    "end";

    public SaveTokenScript(RedissonReactiveClient redissonClient) {
        this.redissonClient = redissonClient;
        this.scriptSha = redissonClient.getScript()
                .scriptLoad(SCRIPT)
                .block();
    }

    public Mono<TokenStatus> saveToken(InsertWaitingQueueTokenCommand command) {
        List<Object> keys = Arrays.asList(
                command.getWaitingQueueName(),
                command.getWorkingQueueName(),
                command.getTokenValue()
        );
        List<Object> argv = Arrays.asList(
                command.getWorkingQueueMaxSlots(),
                command.getTokenValue(),
                command.getEnterTime(),
                command.getCacheValue(),
                command.getTtlInMinutes() * 60
        );

        return redissonClient.getScript(StringCodec.INSTANCE)
                .evalSha(
                        RScript.Mode.READ_WRITE,
                        scriptSha,
                        RScript.ReturnType.VALUE,
                        keys,
                        argv.toArray()
                )
                .map(result -> (result.equals(1L) ? TokenStatus.WORKING : TokenStatus.WAITING));
    }

}