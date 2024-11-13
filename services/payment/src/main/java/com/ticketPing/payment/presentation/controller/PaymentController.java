package com.ticketPing.payment.presentation.controller;

import static response.CommonResponse.success;

import com.ticketPing.payment.application.dto.PaymentResponse;
import com.ticketPing.payment.application.service.PaymentService;
import response.CommonResponse;
import io.swagger.v3.oas.annotations.Operation;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/payments")
public class PaymentController {

    private final PaymentService paymentService;

    @Operation(summary = "PG사 결제 요청")
    @PostMapping
    public ResponseEntity<CommonResponse<PaymentResponse>> requestPayment(@RequestHeader("X-User-Id") UUID userId,
                                                                          @RequestParam("orderId") UUID orderId) {
        return ResponseEntity
                .status(201)
                .body(success(paymentService.requestPayment(userId, orderId)));
    }

    @Operation(summary = "결제 상태 확인")
    @GetMapping("/{paymentId}")
    public ResponseEntity<CommonResponse<PaymentResponse>> checkPaymentStatus(@RequestParam("paymentId") UUID paymentId) {
        return ResponseEntity
                .status(201)
                .body(success(paymentService.checkPaymentStatus(paymentId)));
    }

}