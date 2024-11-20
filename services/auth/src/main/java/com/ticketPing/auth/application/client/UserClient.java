package com.ticketPing.auth.application.client;

import java.util.UUID;

import response.CommonResponse;
import user.UserLookupRequest;
import user.UserResponse;

public interface UserClient {
    CommonResponse<UserResponse> getUserByEmailAndPassword(UserLookupRequest userLookupRequest);

    CommonResponse<UserResponse> getUser(UUID userId);
}
