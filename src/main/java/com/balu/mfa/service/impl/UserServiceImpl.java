package com.balu.mfa.service.impl;

import com.balu.mfa.dto.AuthRequest;
import com.balu.mfa.entity.User;
import com.balu.mfa.repository.UserRepository;
import com.balu.mfa.service.base.UserService;
import dev.samstevens.totp.secret.DefaultSecretGenerator;
import dev.samstevens.totp.secret.SecretGenerator;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.FactorGrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final MfaService mfaService;

    @Override
    public String registerUser(AuthRequest request) {

        if (userRepository.findByUserName(request.getUserName()).isPresent()) {
            throw new RuntimeException("Username already exists");
        }

        String hashedPassword = passwordEncoder.encode(request.getPassword());
        SecretGenerator secretGenerator = new DefaultSecretGenerator();
        String secret = secretGenerator.generate();

        User user = User.builder()
                .userName(request.getUserName())
                .password(hashedPassword)
                .mfaSecret(secret)
                .build();

        String qrCode = mfaService.generateQrCode(
                user.getUserName(),
                user.getMfaSecret()
        );

        userRepository.save(user);
        return qrCode;
    }

    @Override
    public Optional<User> findByUserName(String userName) {
        return userRepository.findByUserName(userName);
    }

    @Override
    public @NonNull UserDetails loadUserByUsername(@NonNull String username) throws UsernameNotFoundException {
        User user = userRepository
                .findByUserName(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUserName())
                .password(user.getPassword())
                .authorities("USER")
                .build();
    }

    @Override
    public void completeLogin(String username) {
        Authentication currentAuth = SecurityContextHolder
                .getContext()
                .getAuthentication();
        if (currentAuth == null || !currentAuth.isAuthenticated()) {
            throw new IllegalStateException("User must be authenticated with password before completing MFA");
        }

        Object principal = currentAuth.getPrincipal();
        if (principal == null) {
            throw new IllegalStateException("Authentication principal cannot be null");
        }

        List<GrantedAuthority> authorities =
                new ArrayList<>(currentAuth.getAuthorities());

        authorities.add(new SimpleGrantedAuthority(FactorGrantedAuthority.OTT_AUTHORITY));

        Authentication newAuth =
                new UsernamePasswordAuthenticationToken(
                        currentAuth.getPrincipal(),
                        currentAuth.getCredentials(),
                        authorities
                );

        SecurityContextHolder.getContext().setAuthentication(newAuth);
    }
}
