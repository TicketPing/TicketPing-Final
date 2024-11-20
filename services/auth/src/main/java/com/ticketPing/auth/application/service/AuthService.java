package com.ticketPing.auth.application.service;

import auth.UserCacheDto;
import com.ticketPing.auth.application.client.UserClient;
import com.ticketPing.auth.application.dto.LoginResponse;
import com.ticketPing.auth.application.service.enums.Role;
import com.ticketPing.auth.exception.AuthErrorCase;
import com.ticketPing.auth.presentation.request.LoginRequest;
import exception.ApplicationException;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import user.UserLookupRequest;
import user.UserResponse;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final TokenService tokenService;
    private final UserClient userClient;

    public LoginResponse login(LoginRequest loginRequest) {
        UserLookupRequest request = new UserLookupRequest(loginRequest.email(), loginRequest.password());
        UserResponse userResponse = userClient.getUserByEmailAndPassword(request).getData();

        String jwtToken = tokenService.createToken(String.valueOf(userResponse.userId()), Role.USER);

        return new LoginResponse(jwtToken);
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
