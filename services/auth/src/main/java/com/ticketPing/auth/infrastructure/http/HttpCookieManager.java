package com.ticketPing.auth.infrastructure.http;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Optional;

@Service
public class HttpCookieManager {

    public void setCookie(HttpServletResponse response, String key, String value, int expiration) {
        ResponseCookie refreshTokenCookie = ResponseCookie.from(key, value)
                .httpOnly(true)                // XSS 방어
                .secure(true)                  // HTTPS에서만 전송
                .path("/")                     // 모든 경로에서 유효
                .maxAge(expiration)
                .sameSite("Strict")            // CSRF 방어
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString());
    }

    public Optional<String> getCookie(HttpServletRequest request, String key) {
        return Arrays.stream(request.getCookies())
                .filter(cookie -> key.equals(cookie.getName()))
                .findFirst()
                .map(Cookie::getValue);
    }

    public void deleteCookie(HttpServletResponse response, String key) {
        Cookie cookie = new Cookie(key, null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
    }
}
