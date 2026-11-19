package com.github.pepega.authentication.security.model;

import java.util.Objects;
import java.util.UUID;

public record TokenPayload(
        UUID userId,
        TokenType tokenType,
        UUID jti
        ) {
        public TokenPayload {
                Objects.requireNonNull(userId, "userId");
                Objects.requireNonNull(tokenType, "tokenType");
                Objects.requireNonNull(jti, "jti");
        }
}
