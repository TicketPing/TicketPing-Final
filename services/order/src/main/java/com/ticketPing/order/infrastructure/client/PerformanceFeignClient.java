package com.ticketPing.order.infrastructure.client;


import com.ticketPing.order.application.client.PerformanceClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import performance.OrderSeatResponse;
import performance.SeatResponse;
import response.CommonResponse;
import java.util.UUID;
import org.springframework.http.ResponseEntity;

@FeignClient(name = "performance")
public interface PerformanceFeignClient extends PerformanceClient {

    @GetMapping("/api/v1/seats/{seatId}/order-info")
    ResponseEntity<CommonResponse<OrderSeatResponse>> getOrderInfo(@RequestHeader("X_USER_ID") UUID userId,
                                                                   @RequestParam("scheduleId") UUID scheduleId,
                                                                   @PathVariable("seatId") UUID seatId);

    @PutMapping("/api/v1/seats/{seatId}")
    ResponseEntity<CommonResponse<SeatResponse>> updateSeatState(@PathVariable("seatId") UUID seatId,
                                                                 @RequestParam("seatState") Boolean seatState);
}

