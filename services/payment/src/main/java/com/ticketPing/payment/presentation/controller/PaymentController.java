package com.ticketPing.payment.presentation.controller;

import static response.CommonResponse.success;

import com.ticketPing.payment.application.dto.PaymentResponse;
import com.ticketPing.payment.application.service.PaymentApplicationService;
import com.ticketPing.payment.presentation.request.PaymentConfirmRequest;
import jakarta.validation.Valid;
import org.json.simple.JSONObject;
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

    @Operation(summary = "TOSS 결제 상태 확인")
    @PostMapping("/confirm")
    public ResponseEntity<CommonResponse<JSONObject>> confirmPayment(@Valid @RequestHeader("X_USER_ID") UUID userId,
                                                                     @Valid @RequestBody PaymentConfirmRequest request) {
        return ResponseEntity
                .status(200)
                .body(success(paymentApplicationService.confirmPayment(userId, request)));
    }

    @Operation(summary = "결제 단일 조회 (Feign)")
    @GetMapping("/{paymentId}")
    public ResponseEntity<CommonResponse<PaymentResponse>> getPayment(
            @Valid @PathVariable("paymentId") UUID paymentId) {
        return ResponseEntity
                .status(200)
                .body(success(paymentApplicationService.getPayment(paymentId)));
    }

}