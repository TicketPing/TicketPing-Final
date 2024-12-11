package com.ticketPing.user.application.service;

import com.ticketPing.user.application.dto.UserResponse;
import com.ticketPing.user.common.exception.UserErrorCase;
import com.ticketPing.user.domain.model.entity.User;
import com.ticketPing.user.domain.repository.UserRepository;
import com.ticketPing.user.presentation.request.CreateUserRequest;
import exception.ApplicationException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import user.UserLookupRequest;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserResponse createUser(CreateUserRequest request) {
        validateDuplicateEmail(request.email());
        String encodedPassword = passwordEncoder.encode(request.password());
        User savedUser = userRepository.save(User.from(request, encodedPassword));
        return UserResponse.of(savedUser);
    }

    @Transactional(readOnly = true)
    public UserResponse getUser(UUID userId) {
        User user = findUserById(userId);
        return UserResponse.of(user);
    }

    @Transactional(readOnly = true)
    public UserResponse getUserByEmailAndPassword(UserLookupRequest request) {
        User user = findUserByEmail(request.email());
        validatePassword(request.password(), user.getPassword());
        return UserResponse.of(user);
    }

    private void validateDuplicateEmail(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new ApplicationException(UserErrorCase.DUPLICATE_EMAIL);
        }
    }

    private void validatePassword(String rawPassword, String encodedPassword) {
        if (!passwordEncoder.matches(rawPassword, encodedPassword)) {
            throw new ApplicationException(UserErrorCase.PASSWORD_NOT_EQUAL);
        }
    }

    private User findUserById(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ApplicationException(UserErrorCase.USER_NOT_FOUND));
    }

    private User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ApplicationException(UserErrorCase.USER_NOT_FOUND));
    }
}