package com.ticketPing.performance.infrastructure.service;

import com.ticketPing.performance.application.dtos.SeatResponse;
import com.ticketPing.performance.domain.model.entity.Seat;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RedissonClient;
import org.redisson.codec.JsonJacksonCodec;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CacheService {

    private final RedissonClient redissonClient;

    public void cacheSeats(UUID scheduleId, List<Seat> seats) {
        Map<String, SeatResponse> seatMap = seats.stream()
                .collect(Collectors.toMap(seat -> seat.getId().toString(), SeatResponse::of));

        String key = "seat:{" + scheduleId + "}";
        redissonClient.getMap(key, JsonJacksonCodec.INSTANCE).putAll(seatMap);
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
