package com.ticketPing.gateway.infrastructure.client;

import auth.UserCacheDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "auth")
public interface AuthClient {
    @GetMapping("/validate")
    ResponseEntity<UserCacheDto> validateToken(String token);
}
