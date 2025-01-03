package com.ticketPing.order.infrastructure.client;

import com.ticketPing.order.application.client.PaymentClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import payment.PaymentResponse;
import response.CommonResponse;

import java.util.UUID;

@FeignClient(name = "payment")
public interface PaymentFeignClient extends PaymentClient {
    @GetMapping("/api/v1/payments/completed")
    ResponseEntity<CommonResponse<PaymentResponse>> getCompletedPaymentByOrderId(@RequestParam("orderId") UUID orderId);

}
