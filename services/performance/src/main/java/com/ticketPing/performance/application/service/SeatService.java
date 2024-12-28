package com.ticketPing.performance.application.service;

import com.ticketPing.performance.application.dtos.OrderInfoResponse;
import com.ticketPing.performance.application.dtos.SeatResponse;
import com.ticketPing.performance.common.exception.SeatExceptionCase;
import com.ticketPing.performance.domain.model.entity.Schedule;
import com.ticketPing.performance.domain.model.entity.Seat;
import com.ticketPing.performance.domain.model.enums.SeatStatus;
import com.ticketPing.performance.domain.repository.SeatRepository;
import com.ticketPing.performance.infrastructure.service.CacheService;
import exception.ApplicationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SeatService {

    private final SeatRepository seatRepository;
    private final CacheService cacheService;

    public SeatResponse getSeat(UUID id) {
        Seat seat = seatRepository.findByIdWithSeatCost(id)
                .orElseThrow(() -> new ApplicationException(SeatExceptionCase.SEAT_NOT_FOUND));
        return SeatResponse.of(seat);
    }

    public OrderInfoResponse getOrderInfo(UUID seatId) {
        Seat seat = seatRepository.findByIdWithAll(seatId)
                .orElseThrow(() -> new ApplicationException(SeatExceptionCase.SEAT_NOT_FOUND));
        return OrderInfoResponse.of(seat);
    }

    public long cacheSeatsForSchedule(Schedule schedule) {
        List<Seat> seats = seatRepository.findByScheduleWithSeatCost(schedule);

        cacheService.cacheSeats(schedule.getId(), seats);

        return seats.stream()
                .filter(seat -> seat.getSeatStatus() == SeatStatus.AVAILABLE)
                .count();
    }

    public void cacheAvailableSeatsForPerformance(UUID performanceId, long availableSeats) {
        cacheService.cacheAvailableSeats(performanceId, availableSeats);
    }
}

