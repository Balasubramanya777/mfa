package com.balu.mfa.service.base;

import com.balu.mfa.dto.AuthRequest;
import com.balu.mfa.entity.User;

import java.util.Optional;

public interface UserService {
    String registerUser(AuthRequest request);

    Optional<User> findByUserName(String userName);

    void completeLogin(String username);
}
