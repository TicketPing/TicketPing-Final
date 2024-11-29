package com.ticketPing.auth.presentation.controller;

import com.ticketPing.auth.application.dto.LoginResponse;
import auth.UserCacheDto;
import com.ticketPing.auth.application.service.AuthService;
import com.ticketPing.auth.presentation.request.LoginRequest;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import response.CommonResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "사용자 로그인")
    @PostMapping("/login")
    public ResponseEntity<CommonResponse<LoginResponse>> login(@RequestBody @Valid final LoginRequest loginRequest) {
        LoginResponse loginResponse = authService.login(loginRequest);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(CommonResponse.success(loginResponse));
    }

    @Operation(summary = "토큰 검증")
    @PostMapping("/validate")
    public ResponseEntity<CommonResponse<UserCacheDto>> validateToken(@RequestHeader("Authorization") String token) {
        UserCacheDto response = authService.validateToken(token);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(CommonResponse.success(response));
    }

    @Operation(summary = "토큰 재발급")
    @PostMapping("/refresh")
    public ResponseEntity<CommonResponse<LoginResponse>> getUser(@RequestHeader("Authorization") String token) {
        LoginResponse loginResponse = authService.refreshAccessToken(token);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(CommonResponse.success(loginResponse));
    }

}
