package com.balu.mfa.config;

import dev.samstevens.totp.code.*;
import dev.samstevens.totp.secret.DefaultSecretGenerator;
import dev.samstevens.totp.secret.SecretGenerator;
import dev.samstevens.totp.time.SystemTimeProvider;
import dev.samstevens.totp.time.TimeProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MfaConfig {

    @Bean
    public SecretGenerator secretGenerator() {
        return new DefaultSecretGenerator();
    }

    @Bean
    public CodeGenerator codeGenerator(MfaProperties properties) {
        return new DefaultCodeGenerator(HashingAlgorithm.SHA1, properties.getDigits());
    }

    @Bean
    public CodeVerifier codeVerifier(CodeGenerator codeGenerator, MfaProperties properties) {

        TimeProvider timeProvider = new SystemTimeProvider();

        DefaultCodeVerifier verifier = new DefaultCodeVerifier(codeGenerator, timeProvider);

        verifier.setTimePeriod(properties.getTimePeriod());
        verifier.setAllowedTimePeriodDiscrepancy(properties.getAllowedDiscrepancy());

        return verifier;
    }
}
