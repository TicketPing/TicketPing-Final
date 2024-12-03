package com.ticketPing.auth.infrastructure.jwt;

import com.ticketPing.auth.common.exception.AuthErrorCase;
import com.ticketPing.auth.common.enums.Role;
import exception.ApplicationException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

import static com.ticketPing.auth.common.constants.AuthConstants.BEARER_PREFIX;

@Component
public class JwtTokenProvider {

    private final Key secretKey;
    private final long accessTokenExpiration;
    private final long refreshTokenExpiration;

    public JwtTokenProvider(@Value("${jwt.secret}") String secret,
                            @Value("${jwt.accessToken.expiration}") long accessTokenExpiration,
                            @Value("${jwt.refreshToken.expiration}") long refreshTokenExpiration) {
        byte[] bytes = Base64.getDecoder().decode(secret);
        this.secretKey = Keys.hmacShaKeyFor(bytes);
        this.accessTokenExpiration = accessTokenExpiration;
        this.refreshTokenExpiration = refreshTokenExpiration;
    }

    public String createAccessToken(UUID userId, Role role) {
        Date now = new Date();
        return Jwts.builder()
                        .setSubject(userId.toString())
                        .claim("role", role)
                        .setIssuedAt(now)
                        .setExpiration(new Date(now.getTime() + accessTokenExpiration))
                        .signWith(this.secretKey, SignatureAlgorithm.HS256)
                        .compact();
    }

    public String createRefreshToken(UUID userId) {
        Date now = new Date();
        return Jwts.builder()
                        .setSubject(userId.toString())
                        .setIssuedAt(now)
                        .setExpiration(new Date(now.getTime() + refreshTokenExpiration))
                        .signWith(this.secretKey, SignatureAlgorithm.HS256)
                        .compact();
    }

    public String parseToken(String authHeader) {
        if (authHeader == null || !authHeader.startsWith(BEARER_PREFIX)) {
            throw new ApplicationException(AuthErrorCase.INVALID_TOKEN);
        }
        return authHeader.substring(7);
    }

    public Claims validateAndExtractClaims(String token) {
        validateToken(token);
        return getClaimsFromToken(token);
    }

    public void validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token);
        } catch (ExpiredJwtException e) {
            throw new ApplicationException(AuthErrorCase.EXPIRED_TOKEN);
        } catch (Exception e) {
            throw new ApplicationException(AuthErrorCase.INVALID_TOKEN);
        }
    }

    private Claims getClaimsFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody();
    }

    public UUID getUserId(Claims claims) {
        try {
            return UUID.fromString(claims.getSubject());
        } catch (Exception e) {
            throw new ApplicationException(AuthErrorCase.INVALID_TOKEN);
        }
    }

    public Role getUserRole(Claims claims) {
        try {
            return Role.getRole(claims.get("role", String.class));
        } catch (NullPointerException e) {
            throw new ApplicationException(AuthErrorCase.INVALID_TOKEN);
        }
    }
}
