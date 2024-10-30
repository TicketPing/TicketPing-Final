package com.ticketPing.queue_manage.infrastructure.repository.script;

import com.ticketPing.queue_manage.domain.command.waitingQueue.DeleteFirstWaitingQueueTokenCommand;
import java.util.Collections;
import java.util.List;
import org.redisson.api.RScript;
import org.redisson.api.RedissonReactiveClient;
import org.redisson.client.codec.StringCodec;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class DeleteFirstTokenScript {

    private final RedissonReactiveClient redissonClient;
    private final String scriptSha;

    private static final String SCRIPT =
            // 변수 선언
            "local queueName = KEYS[1] " +
                    // 대기열 첫 번째 토큰 조회
                    "local firstMember = redis.call('ZRANGE', queueName, 0, 0) " +
                    "if #firstMember == 0 then " +
                    "    return nil " +
                    "end " +
                    // 대기열 첫 번째 토큰 삭제
                    "redis.call('ZREM', queueName, firstMember[1]) " +
                    "return firstMember[1]";

    public DeleteFirstTokenScript(RedissonReactiveClient redissonClient) {
        this.redissonClient = redissonClient;
        this.scriptSha = redissonClient.getScript()
                .scriptLoad(SCRIPT)
                .block();
    }

    public Mono<String> deleteFirstToken(DeleteFirstWaitingQueueTokenCommand command) {
        List<Object> keys = Collections.singletonList(command.getQueueName());

        return redissonClient.getScript(StringCodec.INSTANCE)
                .evalSha(
                        RScript.Mode.READ_WRITE,
                        scriptSha,
                        RScript.ReturnType.VALUE,
                        keys
                )
                .map(Object::toString);
    }

}