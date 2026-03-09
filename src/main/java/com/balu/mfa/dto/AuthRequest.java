package com.balu.mfa.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AuthRequest {
    @NotBlank
    private String userName;

    @NotBlank
    private String password;
}
