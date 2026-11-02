package com.github.pepega.authentication.security.model;

import java.util.Objects;

public record TokenPayload(
        Long userId,
        TokenType tokenType,
        String jti
        ) {
        public TokenPayload {
                Objects.requireNonNull(userId, "userId");
                Objects.requireNonNull(tokenType, "tokenType");
                Objects.requireNonNull(jti, "jti");
        }
}
