package com.ticketPing.order.application.dtos.temp;

import com.ticketPing.order.application.enums.SeatStatus;
import lombok.Data;

import java.util.UUID;

@Data
public class SeatResponse {
    UUID seatId;
    Integer row;
    Integer col;
    String seatState;
    String seatRate;
    Integer cost;

    public void updateSeatState(SeatStatus seatState) {
        this.seatState = seatState.getValue();
    }
}

