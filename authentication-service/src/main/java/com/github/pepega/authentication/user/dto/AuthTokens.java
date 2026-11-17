package com.github.pepega.authentication.user.dto;

public record AuthTokens(
        String accessToken,
        String refreshToken
) {
}
