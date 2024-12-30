package com.ticketPing.performance.domain.model.entity;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.*;

import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder(access = AccessLevel.PRIVATE)
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "@class")
public class SeatCache {
    private UUID id;
    private Integer row;
    private Integer col;
    private String seatStatus;
    private String seatGrade;
    private Integer cost;
    private UUID userId;

    public static SeatCache from(Seat seat) {
        return SeatCache.builder()
                .id(seat.getId())
                .row(seat.getRow())
                .col(seat.getCol())
                .seatStatus(seat.getSeatStatus().getValue())
                .seatGrade(seat.getSeatCost().getSeatGrade())
                .cost(seat.getSeatCost().getCost())
                .build();
    }
}
