package com.ticketPing.performance.infrastructure.service;

import com.ticketPing.performance.application.dtos.SeatResponse;
import com.ticketPing.performance.common.exception.SeatExceptionCase;
import com.ticketPing.performance.domain.model.entity.SeatCache;
import exception.ApplicationException;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.redisson.codec.JsonJacksonCodec;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CacheService {

    private final RedissonClient redissonClient;

    public void cacheSeats(UUID scheduleId, Map<String, SeatCache> seatMap, Duration ttl) {
        String key = "seat:{" + scheduleId + "}";

        RMap<String, SeatCache> seatCache = redissonClient.getMap(key, JsonJacksonCodec.INSTANCE);
        seatCache.putAll(seatMap);
        seatCache.expire(ttl);
    }

    public void cacheAvailableSeats(UUID performanceId, long availableSeats) {
        String key = "availableSeats:" + performanceId;
        redissonClient.getBucket(key).set(availableSeats);
    }

    public Map<String, SeatCache> getSeatsFromCache(UUID scheduleId) {
        String key = "seat:{" + scheduleId + "}";
        return redissonClient.getMap(key, JsonJacksonCodec.INSTANCE);
    }

    public SeatCache getSeatFromCache(UUID scheduleId, UUID seatId) {
        String key = "seat:{" + scheduleId + "}";
        RMap<String, SeatCache> seatCacheMap = redissonClient.getMap(key, JsonJacksonCodec.INSTANCE);

        return Optional.ofNullable(seatCacheMap.get(seatId.toString()))
                .orElseThrow(() -> new ApplicationException(SeatExceptionCase.SEAT_CACHE_NOT_FOUND));
    }
}
