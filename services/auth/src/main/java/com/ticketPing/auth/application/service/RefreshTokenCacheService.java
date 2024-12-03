package com.ticketPing.auth.application.service;

import caching.repository.RedisRepository;
import com.ticketPing.auth.common.exception.AuthErrorCase;
import exception.ApplicationException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenCacheService {

    @Value("${jwt.refreshToken.expiration}")
    private long refreshTokenExpiration;
    private final RedisRepository redisRepository;

    public void saveRefreshToken(UUID userId, String refreshToken) {
        String key = generateKey(userId);
        redisRepository.setValueWithTTL(key, refreshToken, Duration.ofMillis(refreshTokenExpiration));
    }

    public String getRefreshToken(UUID userId) {
        String key = generateKey(userId);
        return Optional.ofNullable(redisRepository.getValueAsClass(key, String.class))
                .orElseThrow(() -> new ApplicationException(AuthErrorCase.REFRESH_TOKEN_NOT_FOUND));
    }

    public void deleteRefreshToken(UUID userId) {
        String key = generateKey(userId);
        redisRepository.deleteKey(key);
    }

    private String generateKey(UUID userId) {
        return String.format("auth:refreshToken:user:%s", userId);
    }

}
