package com.ticketPing.auth.application.service;

import auth.UserCacheDto;
import caching.repository.RedisRepository;
import com.ticketPing.auth.application.client.UserClient;
import com.ticketPing.auth.application.dto.LoginResponse;
import com.ticketPing.auth.application.service.enums.Role;
import com.ticketPing.auth.exception.AuthErrorCase;
import com.ticketPing.auth.presentation.request.LoginRequest;
import exception.ApplicationException;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import user.UserLookupRequest;
import user.UserResponse;

import java.time.Duration;
import java.util.UUID;

@Service
public class AuthService {
    private final TokenService tokenService;
    private final UserClient userClient;
    private final RedisRepository redisRepository;
    private final long refreshTokenExpiration;

    public AuthService(TokenService tokenService, UserClient userClient, RedisRepository redisRepository,
                       @Value("${jwt.refreshToken.expiration}") long refreshTokenExpiration) {
        this.tokenService = tokenService;
        this.userClient = userClient;
        this.redisRepository = redisRepository;
        this.refreshTokenExpiration = refreshTokenExpiration;
    }

    public LoginResponse login(LoginRequest loginRequest) {
        UserLookupRequest request = new UserLookupRequest(loginRequest.email(), loginRequest.password());
        UserResponse userResponse = userClient.getUserByEmailAndPassword(request).getData();

        String accessToken = tokenService.createAccessToken(String.valueOf(userResponse.userId()), Role.USER);
        createRefreshToken(String.valueOf(userResponse.userId()));

        return new LoginResponse(accessToken);
    }

    private void createRefreshToken(String userId) {
        String refreshToken = tokenService.createRefreshToken(userId);
        String key = "user:" + userId + ":refresh_token";
        redisRepository.setValueWithTTL(key , refreshToken, Duration.ofMillis(refreshTokenExpiration));
    }

    public UserCacheDto validateToken(String jwtToken) {
        Claims claims = tokenService.extractClaims(jwtToken);

        UUID userId = parseUserId(claims);
        Role role = parseUserRole((claims));

        validateUser(userId, role);

        return new UserCacheDto(userId, role.getValue());
    }

    private UUID parseUserId(Claims claims) {
        try {
            return UUID.fromString(claims.getSubject());
        } catch (IllegalArgumentException e) {
            throw new ApplicationException(AuthErrorCase.INVALID_USER_ID);
        }
    }

    private Role parseUserRole(Claims claims) {
        try {
            return Role.valueOf(claims.get("auth", String.class));
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new ApplicationException(AuthErrorCase.INVALID_ROLE);
        }
    }

    private void validateUser(UUID userId, Role role) {
        if(role.equals(Role.USER)) {
            userClient.getUser(userId).getData();
        } else {
            throw new ApplicationException(AuthErrorCase.INVALID_ROLE);
        }
    }
}
