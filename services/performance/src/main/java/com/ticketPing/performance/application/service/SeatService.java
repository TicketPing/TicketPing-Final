package com.ticketPing.performance.application.service;

import caching.repository.RedisRepository;
import com.ticketPing.performance.application.dtos.OrderInfoResponse;
import com.ticketPing.performance.application.dtos.SeatResponse;
import com.ticketPing.performance.domain.model.entity.Schedule;
import com.ticketPing.performance.domain.model.entity.Seat;
import com.ticketPing.performance.domain.repository.SeatRepository;
import com.ticketPing.performance.presentation.cases.exception.SeatExceptionCase;
import exception.ApplicationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static caching.enums.RedisKeyPrefix.AVAILABLE_SEATS;
import static caching.enums.RedisKeyPrefix.SEAT_CACHE;

@Service
@RequiredArgsConstructor
public class SeatService {
    private final SeatRepository seatRepository;
    private final RedisRepository redisRepository;  // TODO: 상위 서비스 만들어서 불러오기

    @Transactional(readOnly = true)
    public SeatResponse getSeat(UUID id) {
        Seat seat = findSeatByIdJoinSeatCost(id);
        return SeatResponse.of(seat);
    }

    @Transactional
    public SeatResponse updateSeatState(UUID seatId, Boolean seatState) {
        Seat seat = findSeatByIdJoinSeatCost(seatId);
        seat.updateSeatState(seatState);
        return SeatResponse.of(seat);
    }

    @Transactional
    public Seat findSeatByIdJoinSeatCost(UUID id) {
        return seatRepository.findByIdJoinSeatCost(id)
                .orElseThrow(() -> new ApplicationException(SeatExceptionCase.SEAT_NOT_FOUND));
    }

    @Transactional
    public OrderInfoResponse getOrderInfo(UUID seatId) {
        Seat seat = seatRepository.findByIdJoinAll(seatId)
                .orElseThrow(() -> new ApplicationException(SeatExceptionCase.SEAT_NOT_FOUND));
        return OrderInfoResponse.of(seat);
    }

    @Transactional
    public List<SeatResponse> getAllScheduleSeats(UUID scheduleId) {
        Set<String> ids = redisRepository.getKeys(SEAT_CACHE.getValue() + scheduleId + ":*");
        if(ids.isEmpty()) {
            throw new ApplicationException(SeatExceptionCase.SEAT_CACHE_NOT_FOUND);
        }
        return redisRepository.getValuesAsClass(ids.stream().toList(), SeatResponse.class);
    }

    @Transactional
    public void createSeatsCache(List<Schedule> schedules, UUID performanceId) {
        long availableSeats = 0;

        for(Schedule schedule : schedules) {
            List<Seat> seats = findSeatsByScheduleJoinSeatCost(schedule);

            availableSeats += seats.stream().filter(s -> !s.getSeatState()).count();

            // 좌석 캐싱
            String prefix = SEAT_CACHE.getValue() + schedule.getId() + ":";
            Map<String, Object> seatMap = new HashMap<>();
            seats.forEach(seat -> {seatMap.put(prefix+seat.getId(), SeatResponse.of(seat));});
            redisRepository.setValues(seatMap);
        }

        // counter 생성
        redisRepository.setValue(AVAILABLE_SEATS.getValue() + performanceId, availableSeats);
    }

    @Transactional
    public List<Seat> findSeatsByScheduleJoinSeatCost(Schedule schedule) {
        return seatRepository.findByScheduleJoinSeatCost(schedule);
    }
}
