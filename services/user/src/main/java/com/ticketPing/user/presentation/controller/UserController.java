package com.ticketPing.user.presentation.controller;

import com.ticketPing.user.application.dto.UserResponse;
import com.ticketPing.user.application.service.UserService;
import com.ticketPing.user.presentation.cases.UserSuccessCase;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.ticketPing.user.presentation.request.CreateUserRequest;


import java.util.UUID;
import response.CommonResponse;
import user.UserLookupRequest;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    @Operation(summary = "회원 가입")
    @PostMapping("/signup")
    public ResponseEntity<CommonResponse<UserResponse>> createUser(@RequestBody @Valid CreateUserRequest request) {
        UserResponse userResponse = userService.createUser(request);
        return ResponseEntity
                .status(201)
                .body(CommonResponse.success(UserSuccessCase.SUCCESS_CREATE_USER, userResponse));
    }

    @Operation(summary = "로그인 사용자 정보 확인")
    @PostMapping("/login")
    public ResponseEntity<CommonResponse<UserResponse>> getUserByEmailAndPassword(@RequestBody UserLookupRequest request) {
        UserResponse userResponse = userService.getUserByEmailAndPassword(request);
        return ResponseEntity
                .status(200)
                .body(CommonResponse.success(UserSuccessCase.SUCCESS_GET_USER, userResponse));
    }

    @Operation(summary = "사용자 정보 확인")
    @GetMapping("/{userId}")
    public ResponseEntity<CommonResponse<UserResponse>> getUser(@PathVariable("userId") UUID userId) {
        UserResponse userResponse = userService.getUser(userId);
        return ResponseEntity
                .status(200)
                .body(CommonResponse.success(UserSuccessCase.SUCCESS_GET_USER, userResponse));
    }
}
