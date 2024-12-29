package com.ticketPing.performance.application.service;

import com.ticketPing.performance.application.dtos.OrderInfoResponse;
import com.ticketPing.performance.application.dtos.SeatResponse;
import com.ticketPing.performance.common.exception.SeatExceptionCase;
import com.ticketPing.performance.domain.model.entity.Schedule;
import com.ticketPing.performance.domain.model.entity.Seat;
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

    public SeatResponse getSeat(UUID id) {
        Seat seat = seatRepository.findByIdWithSeatCost(id)
                .orElseThrow(() -> new ApplicationException(SeatExceptionCase.SEAT_NOT_FOUND));
        return SeatResponse.of(seat);
    }

    public void preReserveSeat(UUID scheduleId, UUID seatId) {
        luaScriptService.preReserveSeat(scheduleId, seatId);
    }

    public OrderInfoResponse getOrderInfo(UUID seatId) {
        Seat seat = seatRepository.findByIdWithAll(seatId)
                .orElseThrow(() -> new ApplicationException(SeatExceptionCase.SEAT_NOT_FOUND));
        return OrderInfoResponse.of(seat);
    }

    public long cacheSeatsForSchedule(Schedule schedule) {
        List<Seat> seats = seatRepository.findByScheduleWithSeatCost(schedule);

        Map<String, SeatResponse> seatMap = seats.stream()
                .collect(Collectors.toMap(seat -> seat.getId().toString(), SeatResponse::of));

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
}

