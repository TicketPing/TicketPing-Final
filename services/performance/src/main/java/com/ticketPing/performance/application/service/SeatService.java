package com.ticketPing.performance.application.service;

import com.ticketPing.performance.application.dtos.OrderSeatResponse;
import com.ticketPing.performance.application.dtos.SeatResponse;
import com.ticketPing.performance.common.exception.SeatExceptionCase;
import com.ticketPing.performance.domain.model.entity.Schedule;
import com.ticketPing.performance.domain.model.entity.Seat;
import com.ticketPing.performance.domain.model.entity.SeatCache;
import com.ticketPing.performance.domain.model.enums.SeatStatus;
import com.ticketPing.performance.domain.repository.SeatRepository;
import com.ticketPing.performance.infrastructure.service.CacheService;
import com.ticketPing.performance.infrastructure.service.LuaScriptService;
import exception.ApplicationException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SeatService {

    private final SeatRepository seatRepository;
    private final CacheService cacheService;
    private final LuaScriptService luaScriptService;

    @Value("${seat.pre-reserve-ttl}")
    private int PRE_RESERVE_TTL;

    public SeatResponse getSeat(UUID id) {
        Seat seat = seatRepository.findByIdWithSeatCost(id)
                .orElseThrow(() -> new ApplicationException(SeatExceptionCase.SEAT_NOT_FOUND));
        return SeatResponse.of(seat);
    }

    public void preReserveSeat(UUID scheduleId, UUID seatId, UUID userId) {
        luaScriptService.preReserveSeat(scheduleId, seatId, userId);
    }

    public void cancelPreReserveSeat(UUID scheduleId, UUID seatId, UUID userId) {
        validatePreserve(scheduleId, seatId, userId);
        cacheService.canclePreReserveSeat(scheduleId, seatId);
    }

    public OrderSeatResponse getOrderSeatInfo(UUID scheduleId, UUID seatId, UUID userId) {
        validatePreserve(scheduleId, seatId, userId);

        Seat seat = seatRepository.findByIdWithAll(seatId)
                .orElseThrow(() -> new ApplicationException(SeatExceptionCase.SEAT_NOT_FOUND));

        return OrderSeatResponse.of(seat);
    }

    public void extendPreReserveTTL(UUID scheduleId, UUID seatId) {
        cacheService.extendPreReserveTTL(scheduleId, seatId, Duration.ofSeconds(PRE_RESERVE_TTL));
    }

    public long cacheSeatsForSchedule(Schedule schedule) {
        List<Seat> seats = seatRepository.findByScheduleWithSeatCost(schedule);

        Map<String, SeatCache> seatMap = seats.stream()
                .collect(Collectors.toMap(seat -> seat.getId().toString(), SeatCache::from));

        LocalDateTime expiration = schedule.getStartDate().atTime(23, 59, 59);
        Duration ttl = Duration.between(LocalDateTime.now(), expiration);

        cacheService.cacheSeats(schedule.getId(), seatMap, ttl);

        return seats.stream()
                .filter(seat -> seat.getSeatStatus() == SeatStatus.AVAILABLE)
                .count();
    }

    public void cacheAvailableSeatsForPerformance(UUID performanceId, long availableSeats) {
        cacheService.cacheAvailableSeats(performanceId, availableSeats);
    }

    private void validatePreserve(UUID scheduleId, UUID seatId, UUID userId) {
        SeatCache seatCache = cacheService.getSeatFromCache(scheduleId, seatId);

        if(!seatCache.getSeatStatus().equals(SeatStatus.HELD.getValue())) {
            throw new ApplicationException(SeatExceptionCase.SEAT_NOT_PRE_RESERVED);
        } else if(!seatCache.getUserId().equals(userId)) {
            System.out.println(seatCache.getUserId() + " " + userId);
            throw new ApplicationException(SeatExceptionCase.USER_NOT_MATCH);
        }
    }
}

