package com.github.pepega.authentication.exception.exceptions;

import org.springframework.http.HttpStatus;

public class EmptyValueException extends AppException {
    public EmptyValueException(String message, String value) {
        super(
                HttpStatus.BAD_REQUEST,
                message,
                value + " cannot be empty",
                "Empty value",
                "VALUE_IS_REQUIRED"
        );
    }
}
