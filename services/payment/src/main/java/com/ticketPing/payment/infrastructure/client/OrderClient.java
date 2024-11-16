package com.ticketPing.payment.infrastructure.client;

import order.OrderInfoForPaymentResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@FeignClient(name = "order", path = "/api/v1/orders")
public interface OrderClient {
    @GetMapping("/{orderId}")
    OrderInfoForPaymentResponse getOrderInfo(@PathVariable("orderId") UUID orderId,
                                             @RequestParam("userId") UUID userId);
}
