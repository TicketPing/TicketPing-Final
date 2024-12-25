package com.ticketPing.performance.application.dtos;

import com.ticketPing.performance.domain.model.entity.Performance;
import com.ticketPing.performance.domain.model.entity.PerformanceHall;
import com.ticketPing.performance.domain.model.entity.Schedule;
import com.ticketPing.performance.domain.model.entity.Seat;
import lombok.AccessLevel;
import lombok.Builder;

import java.time.LocalDate;
import java.util.UUID;

@Builder(access = AccessLevel.PRIVATE)
public record OrderInfoResponse(
        UUID seatId,
        Integer row,
        Integer col,
        String seatGrade,
        Integer cost,
        UUID scheduleId,
        LocalDate startDate,
        UUID performanceHallId,
        String performanceHallName,
        UUID performanceId,
        String performanceName,
        Integer performanceGrade,
        UUID companyId
) {
    public static OrderInfoResponse of(Seat seat) {
        Schedule schedule = seat.getSchedule();
        Performance performance = schedule.getPerformance();
        PerformanceHall performanceHall = performance.getPerformanceHall();

        return OrderInfoResponse.builder()
                .seatId(seat.getId())
                .row(seat.getRow())
                .col(seat.getCol())
                .seatGrade(seat.getSeatCost().getSeatGrade())
                .cost(seat.getSeatCost().getCost())
                .scheduleId(schedule.getId())
                .startDate(schedule.getStartDate())
                .performanceHallId(performanceHall.getId())
                .performanceHallName(performanceHall.getName())
                .performanceId(performance.getId())
                .performanceName(performance.getName())
                .performanceGrade(performance.getGrade())
                .companyId(performance.getCompanyId())
                .build();
    }
}
