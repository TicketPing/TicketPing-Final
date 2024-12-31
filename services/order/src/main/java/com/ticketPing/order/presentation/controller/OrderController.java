package com.ticketPing.order.presentation.controller;

import com.ticketPing.order.application.dtos.OrderResponse;
import com.ticketPing.order.application.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import response.CommonResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static response.CommonResponse.success;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/orders")
public class OrderController {

    private final OrderService orderService;

    @Operation(summary = "예매 좌석 생성")
    @PostMapping
    public ResponseEntity<CommonResponse<OrderResponse>> createOrder(@RequestHeader("X_USER_ID") UUID userId,
                                                                    @RequestParam("performanceId") UUID performanceId,
                                                                    @RequestParam("scheduleId") UUID scheduleId,
                                                                    @RequestParam("seatId") UUID seatId) {
        OrderResponse orderResponse = orderService.createOrder(scheduleId, seatId, userId);
        return ResponseEntity
                .status(201)
                .body(success(orderResponse));
    }

    @Operation(summary = "사용자 예매 목록 전체 조회")
    @GetMapping("/user/reservations")
    public ResponseEntity<CommonResponse<List<OrderResponse>>> getUserReservation(@RequestHeader("X_USER_ID") UUID userId) {
        List<OrderResponse> userReservationDto = orderService.getUserOrders(userId);
        return ResponseEntity
                .status(200)
                .body(success(userReservationDto));
    }

    @Operation(summary = "주문 정보 검증")
    @PostMapping("/{orderId}/validate")
    public ResponseEntity<CommonResponse<Object>> validateOrder(@RequestHeader("X_USER_ID") UUID userId,
                                                       @RequestParam("performanceId") UUID performanceId,
                                                       @PathVariable("orderId") UUID orderId) {
        orderService.validateOrderAndExtendTTL(orderId, userId);
        return ResponseEntity
                .status(200)
                .body(success());
    }

}
