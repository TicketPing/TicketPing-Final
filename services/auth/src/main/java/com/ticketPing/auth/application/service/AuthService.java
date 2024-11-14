package com.ticketPing.auth.application.service;

import com.ticketPing.auth.application.client.UserClient;
import com.ticketPing.auth.application.dto.LoginResponse;
import com.ticketPing.auth.application.dto.UserCacheDto;
import com.ticketPing.auth.infrastructure.security.JwtUtil;
import com.ticketPing.auth.infrastructure.security.Role;
import com.ticketPing.auth.infrastructure.service.RedisService;
import com.ticketPing.auth.presentation.cases.AuthErrorCase;
import com.ticketPing.auth.presentation.request.AuthLoginRequest;
import exception.ApplicationException;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Date;
import java.util.UUID;
import user.LoginRequest;
import user.UserResponse;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final JwtUtil jwtUtil;
    private final UserClient userClient;
    private final RedisService redisService;

    public LoginResponse login(AuthLoginRequest authLoginRequest) {
        LoginRequest loginRequest = new LoginRequest(authLoginRequest.email(), authLoginRequest.password());
        UserResponse userResponse = userClient.getUserByEmailAndPassword(loginRequest).getData();

        cacheUser(new UserCacheDto(userResponse.userId(), Role.USER.getValue()), Duration.ofSeconds(3600));

        String jwtToken = jwtUtil.createToken(userResponse.userId().toString(), Role.USER);

        return new LoginResponse(jwtToken);
    }

    public UserCacheDto validateToken(String jwtToken) {
        jwtUtil.validateToken(jwtToken);
        Claims claims = jwtUtil.getClaimsFromToken(jwtToken);
        UUID userId = UUID.fromString(claims.getSubject());
        String role = claims.get("auth", String.class);

        validateUser(userId, role);

        UserCacheDto userCacheDto = new UserCacheDto(userId, role);
        cacheUser(userCacheDto, Duration.ofSeconds((claims.getExpiration().getTime() - new Date().getTime()) / 1000));

        return userCacheDto;
    }

    public void validateUser(UUID userId, String role) {
        Role enumRole = Role.valueOf(role);

        if(enumRole.equals(Role.USER)) {
            userClient.getUser(userId);
        } else {
            throw new ApplicationException(AuthErrorCase.INVALID_ROLE);
        }
    }

    public void cacheUser(UserCacheDto userCacheDto, Duration duration) {
        redisService.setValueWithTTL(userCacheDto.userId().toString(), userCacheDto, duration);
    }
}
