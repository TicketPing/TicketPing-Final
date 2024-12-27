package com.ticketPing.order.presentation.controller;

import com.ticketPing.order.presentation.request.OrderCreateDto;
import com.ticketPing.order.application.dtos.OrderResponse;
import com.ticketPing.order.application.service.OrderService;
import response.CommonResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/orders")
public class OrderController {

    private final OrderService orderService;

    @Operation(summary = "예매 좌석 생성 + 좌석 선점")
    @PostMapping
    public CommonResponse<OrderResponse> createOrder(@RequestBody OrderCreateDto requestDto,
                                                     @RequestParam("performanceId") UUID performanceId,
                                                     @RequestHeader("X_USER_ID") UUID userId) {
        OrderResponse orderResponse = orderService.createOrder(requestDto, userId);
        return CommonResponse.success(orderResponse);
    }

    @Operation(summary = "사용자 예매 목록 전체 조회")
    @GetMapping("/user/reservations")
    public CommonResponse<List<OrderResponse>> getUserReservation(@RequestHeader("X_USER_ID") UUID userId) {
        List<OrderResponse> userReservationDto = orderService.getUserOrders(userId);
        return CommonResponse.success(userReservationDto);
    }

    @Operation(summary = "주문 정보 검증")
    @PostMapping("/{orderId}/validate")
    public CommonResponse<OrderResponse> validateOrder(@RequestHeader("X_USER_ID") UUID userId,
                                                                @PathVariable("orderId") UUID orderId) {
        OrderResponse orderResponse = orderService.validateOrder(orderId, userId);
        return CommonResponse.success(orderResponse);
    }

}
