package com.ticketPing.auth.application.service;

import com.ticketPing.auth.common.exception.AuthErrorCase;
import com.ticketPing.auth.infrastructure.http.HttpCookieManager;
import exception.ApplicationException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

import static com.ticketPing.auth.common.constants.AuthConstants.REFRESH_COOKIE;

@Service
@RequiredArgsConstructor
public class RefreshTokenCookieService {

    @Value("${jwt.refreshToken.expiration}")
    private long refreshTokenExpiration;
    private final HttpCookieManager cookieManager;

    public void setRefreshToken(HttpServletResponse response, String refreshToken) {
        cookieManager.setCookie(response, REFRESH_COOKIE, refreshToken, (int) TimeUnit.MILLISECONDS.toSeconds(refreshTokenExpiration));
    }

    public String getRefreshToken(HttpServletRequest request) {
        return cookieManager.getCookie(request, REFRESH_COOKIE)
                .orElseThrow(() -> new ApplicationException(AuthErrorCase.REFRESH_TOKEN_NOT_PROVIDED));
    }

    public void deleteRefreshToken(HttpServletResponse response) {
        cookieManager.deleteCookie(response, REFRESH_COOKIE);
    }
}
