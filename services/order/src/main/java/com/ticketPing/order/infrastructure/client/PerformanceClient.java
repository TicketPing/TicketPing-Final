package com.ticketPing.order.infrastructure.client;


import com.ticketPing.order.application.dtos.OrderInfoResponse;
import com.ticketPing.order.application.dtos.temp.SeatResponse;
import response.CommonResponse;
import java.util.UUID;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "performance")
public interface PerformanceClient {

    @GetMapping("/api/v1/seats/{seatId}/order-info")
    ResponseEntity<CommonResponse<OrderInfoResponse>> getOrderInfo(@PathVariable("seatId") String seatId);

    @PutMapping("/api/v1/seats/{seatId}")
    ResponseEntity<CommonResponse<SeatResponse>> updateSeatState(@PathVariable("seatId") UUID seatId,
                                                                 @RequestParam("seatState") Boolean seatState);
}

