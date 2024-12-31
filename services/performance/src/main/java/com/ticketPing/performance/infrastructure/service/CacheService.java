package com.ticketPing.performance.infrastructure.service;

import com.ticketPing.performance.common.exception.SeatExceptionCase;
import com.ticketPing.performance.domain.model.entity.SeatCache;
import exception.ApplicationException;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RBucket;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.redisson.codec.JsonJacksonCodec;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static caching.enums.RedisKeyPrefix.AVAILABLE_SEATS;

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

    public Map<String, SeatCache> getSeatsFromCache(UUID scheduleId) {
        String key = "seat:{" + scheduleId + "}";
        RMap<String, SeatCache> seatCacheRMap = redissonClient.getMap(key, JsonJacksonCodec.INSTANCE);
        return seatCacheRMap.readAllMap();
    }

    public SeatCache getSeatFromCache(UUID scheduleId, UUID seatId) {
        String key = "seat:{" + scheduleId + "}";
        RMap<String, SeatCache> seatCacheMap = redissonClient.getMap(key, JsonJacksonCodec.INSTANCE);

        return Optional.ofNullable(seatCacheMap.get(seatId.toString()))
                .orElseThrow(() -> new ApplicationException(SeatExceptionCase.SEAT_CACHE_NOT_FOUND));
    }

    public void extendPreReserveTTL(UUID scheduleId, UUID seatId, Duration ttl) {
        String ttlKey = "ttl:{" + scheduleId + "}:" + seatId;
        RBucket<Object> bucket = redissonClient.getBucket(ttlKey);
        if (!bucket.isExists()) {
            throw new ApplicationException(SeatExceptionCase.TTL_NOT_EXIST);
        }
        bucket.expire(ttl);
    }

    public void canclePreReserveSeat(UUID scheduleId, UUID seatId) {
        String ttlKey = "ttl:{" + scheduleId + "}:" + seatId;
        redissonClient.getBucket(ttlKey).delete();

        String key = "seat:{" + scheduleId + "}";
        RMap<String, SeatCache> seatCacheMap = redissonClient.getMap(key, JsonJacksonCodec.INSTANCE);

        SeatCache seatCache = Optional.ofNullable(seatCacheMap.get(seatId.toString()))
                .orElseThrow(() -> new ApplicationException(SeatExceptionCase.SEAT_CACHE_NOT_FOUND));
        seatCache.cancelPreReserveSeat();

        seatCacheMap.put(seatId.toString(), seatCache);
    }

    public void reserveSeat(String scheduleId, String seatId) {
        String ttlKey = "ttl:{" + scheduleId + "}:" + seatId;
        redissonClient.getBucket(ttlKey).delete();

        String key = "seat:{" + scheduleId + "}";
        RMap<String, SeatCache> seatCacheMap = redissonClient.getMap(key, JsonJacksonCodec.INSTANCE);

        SeatCache seatCache = Optional.ofNullable(seatCacheMap.get(seatId.toString()))
                .orElseThrow(() -> new ApplicationException(SeatExceptionCase.SEAT_CACHE_NOT_FOUND));
        seatCache.reserveSeat();

        seatCacheMap.put(seatId.toString(), seatCache);
    }

    public void cacheAvailableSeats(UUID performanceId, long availableSeats) {
        String key = AVAILABLE_SEATS.getValue() + performanceId;
        redissonClient.getBucket(key).set(availableSeats);
    }
}
