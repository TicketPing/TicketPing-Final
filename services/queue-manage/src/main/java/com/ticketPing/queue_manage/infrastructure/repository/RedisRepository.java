package com.ticketPing.queue_manage.infrastructure.repository;

import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RAtomicLongReactive;
import org.redisson.api.RBucketReactive;
import org.redisson.api.RScoredSortedSetReactive;
import org.redisson.api.RedissonReactiveClient;
import org.redisson.client.codec.StringCodec;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
public class RedisRepository {

    private final RedissonReactiveClient redissonClient;

    public Mono<RScoredSortedSetReactive<String>> getSortedSet(String key) {
        return Mono.just(redissonClient.getScoredSortedSet(key, StringCodec.INSTANCE));
    }

    public Mono<RBucketReactive<String>> getBucket(String key) {
        return Mono.just(redissonClient.getBucket(key, StringCodec.INSTANCE));
    }

    public Mono<RAtomicLongReactive> getCounter(String key) {
        return Mono.just(redissonClient.getAtomicLong(key));
    }

}
