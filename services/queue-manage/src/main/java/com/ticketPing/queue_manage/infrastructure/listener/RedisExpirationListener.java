package com.ticketPing.queue_manage.infrastructure.listener;

import static caching.enums.RedisKeyPrefix.TOKEN_VALUE;
import static com.ticketPing.queue_manage.domain.model.enums.WorkingQueueTokenDeleteCase.TOKEN_EXPIRED;

import com.ticketPing.queue_manage.application.service.WorkingQueueService;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class RedisExpirationListener implements MessageListener {

    private final static String LEADER_KEY_PREFIX = "LeaderKey:";

    private final RedissonClient redissonClient;
    private final WorkingQueueService workingQueueService;

    @Override
    public void onMessage(final Message message, final byte[] pattern) {
        String tokenValue = message.toString();

        // 작업열 토큰 확인
        if (tokenValue.startsWith(TOKEN_VALUE.getValue())) {
            log.info("WorkingQueueToken has expired: {}", tokenValue);

            String leaderKey = LEADER_KEY_PREFIX + tokenValue;
            RLock leaderLock = redissonClient.getLock(leaderKey);

            tryToLeader(tokenValue, leaderLock);
        }
    }

    private void tryToLeader(String tokenValue, RLock leaderLock) {
        try {
            if (leaderLock.tryLock(0, 5, TimeUnit.SECONDS)) {
                try {
                    log.info("This instance is the leader, processing job..");
                    workingQueueService.transferToken(TOKEN_EXPIRED, tokenValue);
                } finally {
                    if (leaderLock.isHeldByCurrentThread()) {
                        leaderLock.unlock();
                    }
                }
            } else {
                log.info("Failed to acquire lock for key!!");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

}