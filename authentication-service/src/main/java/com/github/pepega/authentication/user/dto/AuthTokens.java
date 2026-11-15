package com.github.pepega.authentication.user.dto;

public record TokenPairs(
        String accessToken,
        String refreshToken
) {
}
