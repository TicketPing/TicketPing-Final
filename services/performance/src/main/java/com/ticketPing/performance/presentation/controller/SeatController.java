package com.ticketPing.performance.presentation.controller;

import com.ticketPing.performance.application.dtos.OrderInfoResponse;
import com.ticketPing.performance.application.dtos.SeatResponse;
import com.ticketPing.performance.application.service.SeatService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import response.CommonResponse;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/seats")
@RequiredArgsConstructor
public class SeatController {
    private final SeatService seatService;

    @Operation(summary = "좌석 정보 조회")
    @GetMapping("/{seatId}")
    public ResponseEntity<CommonResponse<SeatResponse>> getSeat(@PathVariable("seatId") UUID seatId) {
        SeatResponse seatResponse = seatService.getSeat(seatId);
        return ResponseEntity
                .status(200)
                .body(CommonResponse.success(seatResponse));
    }

    @Operation(summary = "좌석 선점")
    @PostMapping("/{seatId}/pre-reserve")
    public ResponseEntity<CommonResponse<Object>> preReserveSeat(@RequestParam("performanceId") UUID performanceId,
                                                                 @RequestParam("scheduleId") UUID scheduleId,
                                                                 @PathVariable("seatId") UUID seatId)  {
        seatService.preReserveSeat(scheduleId, seatId);
        return ResponseEntity
                .status(200)
                .body(CommonResponse.success());
    }

    // TODO: 좌석 선점 취소

    @Operation(summary = "좌석 주문 정보 조회 (order 서비스에서 호출용)")
    @GetMapping("/{seatId}/order-info")
    public ResponseEntity<CommonResponse<OrderInfoResponse>> getOrderInfo(@PathVariable("seatId") UUID seatId) {
        OrderInfoResponse orderInfoResponse = seatService.getOrderInfo(seatId);
        return ResponseEntity
                .status(200)
                .body(CommonResponse.success(orderInfoResponse));
    }
}
