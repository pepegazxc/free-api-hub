package com.github.pepega.authentication.exception.model;

import org.springframework.http.HttpStatus;

import java.util.Map;

public record ConstraintError(HttpStatus status, String title, String detail, ExceptionCode code, String logContext) {
    public static final Map<String, ConstraintError> CONSTRAINT_ERROR = Map.of(
            "users_hash_email_key", new ConstraintError(
                    HttpStatus.CONFLICT,
                    "Email already exists",
                    "User with this email already exists",
                    ExceptionCode.EMAIL_EXIST,
                    "existing email"
            ),
            "idx_user_username_lower", new ConstraintError(
                    HttpStatus.CONFLICT,
                    "Username already exists",
                    "User with this username already exists",
                    ExceptionCode.USERNAME_TAKEN,
                    "existing username"
            )
    );
}
