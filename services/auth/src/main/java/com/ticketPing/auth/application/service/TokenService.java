package com.ticketPing.auth.application.service;

import com.ticketPing.auth.exception.AuthErrorCase;
import com.ticketPing.auth.application.service.enums.Role;
import exception.ApplicationException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Component
public class TokenService {
    public static final String AUTHORIZATION_KEY = "auth";
    public static final String BEARER_PREFIX = "Bearer ";
    private static final long TOKEN_EXPIRATION = 60 * 60 * 1000L;

    private final Key secretKey;

    public TokenService(@Value("${jwt.secret}") String secret) {
        byte[] bytes = Base64.getDecoder().decode(secret);
        this.secretKey = Keys.hmacShaKeyFor(bytes);
    }

    public String createToken(String userId, Role role) {
        Date now = new Date();
        return BEARER_PREFIX +
                Jwts.builder()
                        .setSubject(userId)
                        .claim(AUTHORIZATION_KEY, role)
                        .setExpiration(new Date(now.getTime() + TOKEN_EXPIRATION))
                        .setIssuedAt(new Date(now.getTime()))
                        .signWith(this.secretKey, SignatureAlgorithm.HS256)
                        .compact();
    }

    public void validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token);
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            throw new ApplicationException(AuthErrorCase.INVALID_TOKEN);
        } catch (ExpiredJwtException e) {
            throw new ApplicationException(AuthErrorCase.EXPIRED_TOKEN);
        } catch (UnsupportedJwtException e) {
            throw new ApplicationException(AuthErrorCase.UNSUPPORTED_AUTHENTICATION);
        } catch (IllegalArgumentException e) {
            throw new ApplicationException(AuthErrorCase.WRONG_TYPE_TOKEN);
        }
    }

    public Claims getClaimsFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody();
    }

    public Claims extractClaims(String jwtToken) {
        validateToken(jwtToken);
        return getClaimsFromToken(jwtToken);
    }
}
