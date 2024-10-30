package com.ticketPing.queue_manage.infrastructure.repository;

import lombok.RequiredArgsConstructor;
import org.redisson.api.RAtomicLongReactive;
import org.redisson.api.RBucketReactive;
import org.redisson.api.RedissonReactiveClient;
import org.redisson.client.codec.StringCodec;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class RedisRepository {

    private final RedissonReactiveClient redissonClient;

    public RBucketReactive<String> getBucket(String key) {
        return redissonClient.getBucket(key, StringCodec.INSTANCE);
    }

    public RAtomicLongReactive getCounter(String key) {
        return redissonClient.getAtomicLong(key);
    }

}
