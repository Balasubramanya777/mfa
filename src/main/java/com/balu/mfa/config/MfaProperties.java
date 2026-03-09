package com.balu.mfa.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
@ConfigurationProperties(prefix = "mfa")
public class MfaProperties {

    private String issuer;
    private int timePeriod;
    private int digits;
    private int allowedDiscrepancy;
}
