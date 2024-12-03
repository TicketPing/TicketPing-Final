package com.ticketPing.auth.infrastructure.client;

import com.ticketPing.auth.application.client.UserClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.UUID;
import response.CommonResponse;
import user.UserLookupRequest;
import user.UserResponse;

@FeignClient(name = "user")
public interface UserFeignClient extends UserClient {
    @GetMapping("/api/v1/users/login")
    CommonResponse<UserResponse> getUserByEmailAndPassword(@RequestBody UserLookupRequest userLookupRequest);
}
