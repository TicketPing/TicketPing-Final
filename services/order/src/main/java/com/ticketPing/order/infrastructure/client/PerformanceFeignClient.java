package com.ticketPing.order.infrastructure.client;


import com.ticketPing.order.application.client.PerformanceClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import performance.OrderSeatResponse;
import response.CommonResponse;

import java.util.UUID;

@FeignClient(name = "performance")
public interface PerformanceFeignClient extends PerformanceClient {

    @GetMapping("/api/v1/client/seats/{seatId}/order-info")
    ResponseEntity<CommonResponse<OrderSeatResponse>> getOrderInfo(@RequestHeader("X_USER_ID") UUID userId,
                                                                   @RequestParam("scheduleId") UUID scheduleId,
                                                                   @PathVariable("seatId") UUID seatId);

    @PostMapping("/api/v1/client/seats/{seatId}/extend-ttl")
    ResponseEntity<CommonResponse<Object>> extendPreReserveTTL(@RequestParam("scheduleId") UUID scheduleId,
                                                               @PathVariable("seatId") UUID seatId);

}

