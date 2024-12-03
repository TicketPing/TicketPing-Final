package com.ticketPing.auth.presentation.controller;

import auth.UserCacheDto;
import com.ticketPing.auth.application.dto.TokenResponse;
import com.ticketPing.auth.application.service.AuthService;
import com.ticketPing.auth.presentation.request.LoginRequest;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import response.CommonResponse;

import java.util.UUID;

import static com.ticketPing.auth.common.constants.AuthConstants.AUTHORIZATION_HEADER;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "사용자 로그인")
    @PostMapping("/login")
    public ResponseEntity<CommonResponse<TokenResponse>> login(@RequestBody @Valid final LoginRequest loginRequest) {
        TokenResponse response = authService.login(loginRequest);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(CommonResponse.success(response));
    }

    @Operation(summary = "토큰 검증")
    @PostMapping("/validate")
    public ResponseEntity<CommonResponse<UserCacheDto>> validateToken(@RequestHeader(AUTHORIZATION_HEADER) String authHeader) {
        UserCacheDto response = authService.validateToken(authHeader);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(CommonResponse.success(response));
    }

    @Operation(summary = "토큰 재발급")
    @PostMapping("/refresh")
    public ResponseEntity<CommonResponse<TokenResponse>> refreshAccessToken(@RequestHeader(AUTHORIZATION_HEADER) String authHeader) {
        TokenResponse response = authService.refreshAccessToken(authHeader);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(CommonResponse.success(response));
    }

    @Operation(summary = "로그아웃")
    @PostMapping("/logout")
    public ResponseEntity<CommonResponse<Object>> logout(@RequestHeader("X_USER_ID") UUID userId) {
        authService.logout(userId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(CommonResponse.success());
    }


}
