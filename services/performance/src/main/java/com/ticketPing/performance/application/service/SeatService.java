package com.ticketPing.performance.application.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ticketPing.performance.application.dtos.OrderInfoResponse;
import com.ticketPing.performance.application.dtos.SeatResponse;
import com.ticketPing.performance.common.exception.SeatExceptionCase;
import com.ticketPing.performance.domain.model.entity.Schedule;
import com.ticketPing.performance.domain.model.entity.Seat;
import com.ticketPing.performance.domain.model.enums.SeatStatus;
import com.ticketPing.performance.domain.repository.SeatRepository;
import exception.ApplicationException;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.redisson.codec.JsonJacksonCodec;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import static caching.enums.RedisKeyPrefix.AVAILABLE_SEATS;

@Service
@RequiredArgsConstructor
public class SeatService {
    private final SeatRepository seatRepository;
    private final RedissonClient redissonClient;

    public SeatResponse getSeat(UUID id) {
        Seat seat = findSeatByIdJoinSeatCost(id);
        return SeatResponse.of(seat);
    }

    @Transactional
    public SeatResponse updateSeatState(UUID seatId, Boolean seatState) {
        Seat seat = findSeatByIdJoinSeatCost(seatId);
        seat.reserveSeat();
        return SeatResponse.of(seat);
    }

    public OrderInfoResponse getOrderInfo(UUID seatId) {
        Seat seat = seatRepository.findByIdJoinAll(seatId)
                .orElseThrow(() -> new ApplicationException(SeatExceptionCase.SEAT_NOT_FOUND));
        return OrderInfoResponse.of(seat);
    }

    public List<SeatResponse> getAllScheduleSeats(UUID scheduleId) {
        JsonJacksonCodec codec = JsonJacksonCodec.INSTANCE;
        String key = "seat:{" + scheduleId +"}";
        RMap<String, SeatResponse> seatMap = redissonClient.getMap(key, codec);
        Map<String, SeatResponse> allSeats = seatMap.readAllMap();
        return allSeats.values().stream().toList();
    }

    public void createSeatsCache(List<Schedule> schedules, UUID performanceId) {
        long availableSeats = 0;

        for(Schedule schedule : schedules) {
            List<Seat> seats = findSeatsByScheduleJoinSeatCost(schedule);
            Map<String, SeatResponse> seatMap = seats.stream()
                    .collect(Collectors.toMap(seat -> String.valueOf(seat.getId()), SeatResponse::of));

            availableSeats += seats.stream()
                    .filter(s -> s.getSeatStatus() == SeatStatus.AVAILABLE)
                    .count();

            JsonJacksonCodec codec = JsonJacksonCodec.INSTANCE;
            String key = "seat:{" + schedule.getId() +"}";
            redissonClient.getMap(key, codec).putAll(seatMap);
        }

        redissonClient.getBucket(AVAILABLE_SEATS.getValue() + performanceId)
                .set(availableSeats);
    }

    @Transactional
    private Seat findSeatByIdJoinSeatCost(UUID id) {
        return seatRepository.findByIdJoinSeatCost(id)
                .orElseThrow(() -> new ApplicationException(SeatExceptionCase.SEAT_NOT_FOUND));
    }

    public List<Seat> findSeatsByScheduleJoinSeatCost(Schedule schedule) {
        return seatRepository.findByScheduleJoinSeatCost(schedule);
    }
}
