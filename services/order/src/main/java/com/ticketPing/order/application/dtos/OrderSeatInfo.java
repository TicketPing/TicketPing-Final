package com.ticketPing.order.application.dtos;

public record OrderSeatInfo (
    String seatId,
    int row,
    int col,
    boolean seatState,
    String seatRate,
    int cost
) {
    public OrderSeatInfo(String seatId, int row, int col, String seatRate, int cost) {
        this(seatId, row, col, false, seatRate, cost);
    }
}

