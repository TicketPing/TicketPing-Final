package com.ticketPing.queue_manage.infrastructure.repository;

import com.ticketPing.queue_manage.domain.command.waitingQueue.DeleteFirstWaitingQueueTokenCommand;
import com.ticketPing.queue_manage.domain.command.waitingQueue.FindWaitingQueueTokenCommand;
import com.ticketPing.queue_manage.domain.command.waitingQueue.InsertWaitingQueueTokenCommand;
import com.ticketPing.queue_manage.domain.model.enums.TokenStatus;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RScript;
import org.redisson.api.RedissonReactiveClient;
import org.redisson.client.codec.StringCodec;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

@Repository
@RequiredArgsConstructor
public class LuaScriptRepository {

    private final RedissonReactiveClient redissonClient;
    private final String saveTokenScript;
    private final String getRankAndSizeScript;
    private final String deleteFirstTokenScript;

    /**
     * 작업열의 인원 여유에 따라 사용자 토큰을 저장하고 토큰의 상태를 가져옵니다.
     **/
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
                        saveTokenScript,
                        RScript.ReturnType.VALUE,
                        keys,
                        argv.toArray()
                )
                .map(result -> (result.equals(1L) ? TokenStatus.WORKING : TokenStatus.WAITING));
    }

    /**
     * 대기열에 저장된 사용자 토큰의 순위와 대기열의 전체 크기를 가져옵니다.
     */
    public Mono<Tuple2<Long, Long>> getRankAndSize(FindWaitingQueueTokenCommand command) {
        List<Object> keys = Collections.singletonList(command.getQueueName());
        List<Object> argv = Collections.singletonList(command.getTokenValue());

        return redissonClient.getScript(StringCodec.INSTANCE)
                .evalSha(
                        RScript.Mode.READ_ONLY,
                        getRankAndSizeScript,
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

    /**
     * 대기열의 첫 번째 토큰을 삭제하고 사용자 토큰 값을 가져옵니다.
     */
    public Mono<String> deleteFirstToken(DeleteFirstWaitingQueueTokenCommand command) {
        List<Object> keys = Collections.singletonList(command.getQueueName());

        return redissonClient.getScript(StringCodec.INSTANCE)
                .evalSha(
                        RScript.Mode.READ_WRITE,
                        deleteFirstTokenScript,
                        RScript.ReturnType.VALUE,
                        keys
                )
                .map(Object::toString);
    }

}
