package com.balu.mfa.service.impl;

import com.balu.mfa.dto.AuthRequest;
import com.balu.mfa.dto.AuthResponse;
import com.balu.mfa.entity.User;
import com.balu.mfa.repository.UserRepository;
import com.balu.mfa.service.base.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Override
    public User registerUser(AuthRequest request) {
        String hashedPassword = passwordEncoder.encode(request.getPassword());
        User user = User.builder().userName(request.getUserName()).password(hashedPassword).build();
        return userRepository.save(user);
    }

    @Override
    public Optional<User> findByUserName(String userName) {
        return userRepository.findByUserName(userName);
    }

    public Optional<User> findByUserId(Long userId) {
        return userRepository.findById(userId);
    }

    @Override
    public AuthResponse logIn(AuthRequest request) {
        User user = userRepository.findByUserName(request.getUserName()).orElseThrow(() -> new IllegalArgumentException("Invalid username or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Invalid username or password");
        }

        String token = jwtService.generateToken(user);
        return AuthResponse.builder().userName(user.getUserName()).token(token).build();
    }
}
