package com.ticketPing.order.application.client;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import performance.OrderSeatResponse;
import performance.SeatResponse;
import response.CommonResponse;

import java.util.UUID;

public interface  PerformanceClient {
    ResponseEntity<CommonResponse<OrderSeatResponse>> getOrderInfo(UUID userId, UUID scheduleId, UUID seatId);

    ResponseEntity<CommonResponse<Object>> extendPreReserveTTL(UUID scheduleId, UUID seatId);

    ResponseEntity<CommonResponse<SeatResponse>> updateSeatState(UUID seatId, Boolean seatState);
}
