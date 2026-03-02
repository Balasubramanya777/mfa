package com.balu.mfa.controller;

import com.balu.mfa.dto.AuthRequest;
import com.balu.mfa.dto.AuthResponse;
import com.balu.mfa.entity.User;
import com.balu.mfa.service.base.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> logIn(@RequestBody AuthRequest request) {
        AuthResponse response = userService.logIn(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody AuthRequest request) {
        User user = userService.registerUser(request);
        return ResponseEntity.ok(user);
    }
}
