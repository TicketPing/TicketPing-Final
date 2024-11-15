package com.ticketPing.order.presentation.request;

import java.util.UUID;

public record OrderCreateDto(
    UUID seatId,
    UUID scheduleId
) {}

