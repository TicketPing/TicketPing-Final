package com.ticketPing.performance.application.dtos;

import com.ticketPing.performance.domain.model.entity.Seat;
import lombok.AccessLevel;
import lombok.Builder;

import java.util.UUID;

@Builder(access = AccessLevel.PRIVATE)
public record SeatResponse (
        UUID seatId,
        Integer row,
        Integer col,
        String seatStatus,
        String seatGrade,
        Integer cost
) {
    public static SeatResponse of(Seat seat) {
        return SeatResponse.builder()
                .seatId(seat.getId())
                .row(seat.getRow())
                .col(seat.getCol())
                .seatStatus(seat.getSeatStatus().getValue())
                .seatGrade(seat.getSeatCost().getSeatGrade())
                .cost(seat.getSeatCost().getCost())
                .build();
    }
}
