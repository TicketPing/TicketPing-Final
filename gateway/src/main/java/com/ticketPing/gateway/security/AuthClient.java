package com.ticketPing.gateway.security;

import auth.UserCacheDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import response.CommonResponse;

@FeignClient(name = "auth")
public interface AuthClient {
    @GetMapping("/validate")
    ResponseEntity<CommonResponse<UserCacheDto>> validateToken(@RequestHeader("Authorization") String token);
}
