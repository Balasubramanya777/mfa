package com.balu.mfa.service.base;

import com.balu.mfa.dto.AuthRequest;
import com.balu.mfa.dto.AuthResponse;
import com.balu.mfa.entity.User;

import java.util.Optional;

public interface UserService {
    User registerUser(AuthRequest request);

    Optional<User> findByUserName(String userName);

    Optional<User> findByUserId(Long userId);

    AuthResponse logIn(AuthRequest request);
}
