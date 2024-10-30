package com.ticketPing.queue_manage.infrastructure.repository.script;

import java.util.Collections;
import java.util.List;
import org.redisson.api.RScript;
import org.redisson.api.RedissonReactiveClient;
import org.redisson.client.codec.StringCodec;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

@Component
public class GetRankAndSizeScript {

    private final RedissonReactiveClient redissonClient;
    private final String scriptSha;

    private static final String SCRIPT =
            // 변수 선언
            "local queueName = KEYS[1] " +
                    "local tokenValue = ARGV[1] " +
                    // 대기열 토큰 순서 조회
                    "local rank = redis.call('ZRANK', queueName, tokenValue) " +
                    "if rank == false then" +
                    "    return nil " +
                    "end " +
                    // 대기열 전체 크기 조회
                    "local size = redis.call('ZCARD', queueName) " +
                    "return {rank, size}";

    public GetRankAndSizeScript(RedissonReactiveClient redissonClient) {
        this.redissonClient = redissonClient;
        this.scriptSha = redissonClient.getScript()
                .scriptLoad(SCRIPT)
                .block();
    }

    public Mono<Tuple2<Long, Long>> getRankAndSize(String queueName, String tokenValue) {
        List<Object> keys = Collections.singletonList(queueName);
        List<Object> argv = Collections.singletonList(tokenValue);

        return redissonClient.getScript(StringCodec.INSTANCE)
                .evalSha(
                        RScript.Mode.READ_ONLY,
                        scriptSha,
                        RScript.ReturnType.MULTI,
                        keys,
                        argv.toArray()
                )
                .map(result -> {
                    List<?> values = (List<?>) result;
                    Long rank = Long.parseLong(String.valueOf(values.get(0)));
                    Long size = Long.parseLong(String.valueOf(values.get(1)));
                    return Tuples.of(rank, size);
                });
    }

}
