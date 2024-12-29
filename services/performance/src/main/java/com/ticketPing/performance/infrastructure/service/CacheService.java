package com.ticketPing.performance.infrastructure.service;

import com.ticketPing.performance.application.dtos.SeatResponse;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.redisson.codec.JsonJacksonCodec;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CacheService {

    private final RedissonClient redissonClient;

    public void cacheSeats(UUID scheduleId, Map<String, SeatResponse> seatMap, Duration ttl) {
        String key = "seat:{" + scheduleId + "}";
        RMap<String, SeatResponse> seatCache = redissonClient.getMap(key, JsonJacksonCodec.INSTANCE);
        seatCache.putAll(seatMap);

        if (!ttl.isNegative() && !ttl.isZero()) {
            seatCache.expire(ttl);
        }
    }

    public void cacheAvailableSeats(UUID performanceId, long availableSeats) {
        String key = "availableSeats:" + performanceId;
        redissonClient.getBucket(key).set(availableSeats);
    }

    public List<SeatResponse> getSeatsFromCache(UUID scheduleId) {
        String key = "seat:{" + scheduleId + "}";
        Map<String, SeatResponse> seatMap = redissonClient.getMap(key, JsonJacksonCodec.INSTANCE);
        return seatMap.values().stream().toList();
    }
}
