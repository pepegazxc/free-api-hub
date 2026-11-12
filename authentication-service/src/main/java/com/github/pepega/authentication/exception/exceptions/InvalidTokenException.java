package com.github.pepega.authentication.exception.exceptions;

import com.github.pepega.authentication.exception.model.ExceptionCode;
import org.springframework.http.HttpStatus;

public class InvalidTokenException extends AppException{
    public InvalidTokenException(String message) {
        super(
                HttpStatus.UNAUTHORIZED,
                message,
                "Something went wrong while parsing a token",
                "Invalid token",
                ExceptionCode.INVALID_TOKEN
        );
    }
}
