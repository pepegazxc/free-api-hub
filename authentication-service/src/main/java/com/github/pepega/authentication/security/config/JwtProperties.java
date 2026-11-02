package com.github.pepega.authentication.security.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

@ConfigurationProperties(prefix = "jwt")
public record JwtProperties(
    String issuer,
    Duration accessTtl,
    Duration refreshTtl,
    String privateKey,
    String publicKey
) {
}
