package com.ticketPing.auth.application.service;

import auth.UserCacheDto;
import com.ticketPing.auth.application.client.UserClient;
import com.ticketPing.auth.application.dto.TokenResponse;
import com.ticketPing.auth.common.enums.Role;
import com.ticketPing.auth.infrastructure.jwt.JwtTokenProvider;
import com.ticketPing.auth.presentation.request.LoginRequest;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import user.UserLookupRequest;
import user.UserResponse;

import java.util.UUID;

import static com.ticketPing.auth.common.constants.AuthConstants.BEARER_PREFIX;


@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenCacheService refreshTokenCacheService;
    private final UserClient userClient;

    public TokenResponse login(LoginRequest loginRequest) {
        UUID userId = authenticateUser(loginRequest);
        String accessToken = generateAndCacheTokens(userId, Role.USER);
        return TokenResponse.of(accessToken);
    }

    public UserCacheDto validateToken(String authHeader) {
        String accessToken = jwtTokenProvider.parseToken(authHeader);
        Claims claims = jwtTokenProvider.validateAndExtractClaims(accessToken);
        return extractUserFromClaims(claims);
    }

    public TokenResponse refreshAccessToken(String authHeader) {
        Claims claims = extractClaimsFromExpiredToken(authHeader);
        UUID userId = jwtTokenProvider.getUserId(claims);
        Role userRole = jwtTokenProvider.getUserRole(claims);

        validateRefreshToken(userId);

        String newAccessToken = BEARER_PREFIX + jwtTokenProvider.createAccessToken(userId, userRole);
        return TokenResponse.of(newAccessToken);
    }

    public void logout(UUID userId) {
        refreshTokenCacheService.deleteRefreshToken(userId);
    }

    private UUID authenticateUser(LoginRequest loginRequest) {
        UserLookupRequest request = new UserLookupRequest(loginRequest.email(), loginRequest.password());
        UserResponse userResponse = userClient.getUserByEmailAndPassword(request).getData();
        return userResponse.userId();
    }

    private String generateAndCacheTokens(UUID userId, Role role) {
        String refreshToken = jwtTokenProvider.createRefreshToken(userId);
        refreshTokenCacheService.saveRefreshToken(userId, refreshToken);
        return BEARER_PREFIX + jwtTokenProvider.createAccessToken(userId, role);
    }

    private Claims extractClaimsFromExpiredToken(String authHeader) {
        String accessToken = jwtTokenProvider.parseToken(authHeader);
        try {
            return jwtTokenProvider.validateAndExtractClaims(accessToken);
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    private void validateRefreshToken(UUID userId) {
        String refreshToken = refreshTokenCacheService.getRefreshToken(userId);
        jwtTokenProvider.validateToken(refreshToken);
    }

    private UserCacheDto extractUserFromClaims(Claims claims) {
        UUID userId = jwtTokenProvider.getUserId(claims);
        Role role = jwtTokenProvider.getUserRole(claims);
        return new UserCacheDto(userId, role.getValue());
    }
}