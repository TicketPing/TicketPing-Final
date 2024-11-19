package com.ticketPing.payment.infrastructure.client;

import com.ticketPing.payment.application.client.OrderClient;
import order.OrderInfoForPaymentResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@FeignClient(name = "order", path = "/api/v1/orders")
public interface OrderFeignClient extends OrderClient {
    @GetMapping("/{orderId}")
    OrderInfoForPaymentResponse getOrderInfo(@PathVariable("orderId") UUID orderId,
                                             @RequestParam("userId") UUID userId);
}
