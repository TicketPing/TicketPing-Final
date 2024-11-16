package com.ticketPing.order.application.dtos;

import java.time.LocalDateTime;
import java.util.UUID;

public record OrderInfoResponse(
    UUID seatId,
    int row,
    int col,
    boolean seatState,
    String seatRate,
    int cost,
    UUID scheduleId,
    LocalDateTime startTime,
    UUID performanceHallId,
    String performanceHallName,
    UUID performanceId,
    String performanceName,
    int performanceGrade,
    UUID companyId
) {}
