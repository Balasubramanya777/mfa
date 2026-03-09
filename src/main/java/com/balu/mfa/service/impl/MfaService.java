package com.balu.mfa.service.impl;

import com.balu.mfa.config.MfaProperties;
import dev.samstevens.totp.code.CodeVerifier;
import dev.samstevens.totp.code.HashingAlgorithm;
import dev.samstevens.totp.exceptions.QrGenerationException;
import dev.samstevens.totp.qr.QrData;
import dev.samstevens.totp.qr.QrGenerator;
import dev.samstevens.totp.qr.ZxingPngQrGenerator;
import dev.samstevens.totp.secret.SecretGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Base64;

@Service
@RequiredArgsConstructor
public class MfaService {

    private final MfaProperties properties;
    private final SecretGenerator secretGenerator;
    private final CodeVerifier codeVerifier;

    public String generateSecret() {
        return secretGenerator.generate();
    }

    public String generateQrCode(String username, String secret) {

        validate(username, "Username must not be empty");
        validate(secret, "Secret must not be empty");

        QrData qrData = new QrData.Builder().label(username).secret(secret).issuer(properties.getIssuer()).algorithm(HashingAlgorithm.SHA1).digits(properties.getDigits()).period(properties.getTimePeriod()).build();

        QrGenerator generator = new ZxingPngQrGenerator();

        try {
            byte[] imageData = generator.generate(qrData);
            return "data:image/png;base64," + Base64.getEncoder().encodeToString(imageData);

        } catch (QrGenerationException e) {
            throw new IllegalStateException("Failed to generate MFA QR code", e);
        }
    }

    public boolean verifyCode(String secret, String code) {

        validate(secret, "Secret must not be empty");
        validate(code, "Code must not be empty");

        return codeVerifier.isValidCode(secret, code);
    }

    private void validate(String value, String message) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(message);
        }
    }
}
