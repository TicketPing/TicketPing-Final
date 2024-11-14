package com.ticketPing.auth.application.client;

import java.util.UUID;
import response.CommonResponse;
import user.LoginRequest;
import user.UserResponse;

public interface UserClient {
    CommonResponse<UserResponse> getUserByEmailAndPassword(LoginRequest loginRequest);
    CommonResponse<UserResponse> getUser(UUID userId);
}
