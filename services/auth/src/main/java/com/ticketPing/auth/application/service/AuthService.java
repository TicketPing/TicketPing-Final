package com.ticketPing.auth.application.service;

import auth.UserCacheDto;
import com.ticketPing.auth.application.client.UserClient;
import com.ticketPing.auth.application.dto.TokenResponse;
import com.ticketPing.auth.common.enums.Role;
import com.ticketPing.auth.common.exception.AuthErrorCase;
import com.ticketPing.auth.infrastructure.jwt.JwtTokenProvider;
import com.ticketPing.auth.presentation.request.LoginRequest;
import exception.ApplicationException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
    private final RefreshTokenCookieService refreshTokenCookieService;
    private final UserClient userClient;

    public TokenResponse login(LoginRequest loginRequest, HttpServletResponse response) {
        UUID userId = authenticateUser(loginRequest);
        createAndSaveRefreshToken(userId, response);
        String accessToken = createAccessToken(userId, Role.USER);
        return TokenResponse.of(accessToken);
    }

    public UserCacheDto validateToken(String authHeader) {
        String accessToken = jwtTokenProvider.parseToken(authHeader);
        jwtTokenProvider.validateToken(accessToken);
        Claims claims = jwtTokenProvider.getClaimsFromToken(accessToken);
        return extractUserFromClaims(claims);
    }

    public TokenResponse refreshAccessToken(String authHeader, HttpServletRequest request, HttpServletResponse response) {
        Claims claims = validateAndExtractClaimsFromExpiredToken(authHeader, response);
        UUID userId = jwtTokenProvider.getUserId(claims);
        Role userRole = jwtTokenProvider.getUserRole(claims);

        validateAndRotateRefreshToken(userId, request, response);

        String newAccessToken = createAccessToken(userId, userRole);
        return TokenResponse.of(newAccessToken);
    }

    public void logout(UUID userId, HttpServletResponse response) {
        refreshTokenCacheService.deleteRefreshToken(userId);
        refreshTokenCookieService.deleteRefreshToken(response);
    }

    private UUID authenticateUser(LoginRequest loginRequest) {
        UserLookupRequest request = new UserLookupRequest(loginRequest.email(), loginRequest.password());
        UserResponse userResponse = userClient.getUserByEmailAndPassword(request).getData();
        return userResponse.userId();
    }

    private String createAccessToken(UUID userId, Role role) {
        return BEARER_PREFIX + jwtTokenProvider.createAccessToken(userId, role);
    }

    private void createAndSaveRefreshToken(UUID userId, HttpServletResponse response) {
        String refreshToken = jwtTokenProvider.createRefreshToken(userId);
        refreshTokenCacheService.saveRefreshToken(userId, refreshToken);
        refreshTokenCookieService.setRefreshToken(response, refreshToken);
    }

    private Claims validateAndExtractClaimsFromExpiredToken(String authHeader, HttpServletResponse response) {
        String token = jwtTokenProvider.parseToken(authHeader);
        try {
            Claims claims = jwtTokenProvider.getClaimsFromToken(token);
            UUID userId = jwtTokenProvider.getUserId(claims);
            logout(userId, response);
            throw new ApplicationException(AuthErrorCase.ACCESS_TOKEN_NOT_EXPIRED);
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        } catch (JwtException e) {
            throw new ApplicationException(AuthErrorCase.INVALID_TOKEN);
        }
    }

    private void validateAndRotateRefreshToken(UUID userId, HttpServletRequest request, HttpServletResponse response) {
        String storedRefreshToken = refreshTokenCacheService.getRefreshToken(userId);
        String cookieRefreshToken = refreshTokenCookieService.getRefreshToken(request);
        validateRefreshToken(userId, response, cookieRefreshToken, storedRefreshToken);
        createAndSaveRefreshToken(userId, response);
    }

    private void validateRefreshToken(UUID userId, HttpServletResponse response, String cookieRefreshToken, String storedRefreshToken) {
        if (!cookieRefreshToken.equals(storedRefreshToken)) {
            logout(userId, response);
            throw new ApplicationException(AuthErrorCase.INVALID_REFRESH_TOKEN);
        }
        jwtTokenProvider.validateToken(storedRefreshToken);
    }

    private UserCacheDto extractUserFromClaims(Claims claims) {
        UUID userId = jwtTokenProvider.getUserId(claims);
        Role role = jwtTokenProvider.getUserRole(claims);
        return new UserCacheDto(userId, role.getValue());
    }
}