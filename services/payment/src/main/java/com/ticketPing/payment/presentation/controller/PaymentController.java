package com.ticketPing.payment.presentation.controller;

import static response.CommonResponse.success;

import com.ticketPing.payment.application.dto.PaymentResponse;
import com.ticketPing.payment.application.service.PaymentApplicationService;
import jakarta.validation.Valid;
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

    private final PaymentApplicationService paymentApplicationService;

    @Operation(summary = "PG사 결제 요청")
    @PostMapping
    public ResponseEntity<CommonResponse<PaymentResponse>> requestPayment(
            @Valid @RequestHeader("X-USER-ID") UUID userId,
            @Valid @RequestParam("performanceId") UUID performanceId,
            @Valid @RequestParam("orderId") UUID orderId) {
        return ResponseEntity
                .status(201)
                .body(success(paymentApplicationService.requestPayment(userId, orderId)));
    }

    @Operation(summary = "PG사 결제 상태 확인")
    @GetMapping("/{paymentId}")
    public ResponseEntity<CommonResponse<PaymentResponse>> checkPaymentStatus(
            @Valid @PathVariable("paymentId") UUID paymentId) {
        return ResponseEntity
                .status(200)
                .body(success(paymentApplicationService.checkPaymentStatus(paymentId)));
    }

}