package com.github.pepega.authentication.exception.exceptions;

import org.springframework.http.HttpStatus;

public class InvalidTokenException extends AppException{
    public InvalidTokenException(String message) {
        super(
                HttpStatus.UNAUTHORIZED,
                message,
                "Something went wrong while parsing a token",
                "Invalid token",
                "INVALID_TOKEN"
        );
    }
}
