package com.github.pepega.authentication.exception.exceptions;

import com.github.pepega.authentication.exception.model.ExceptionCode;
import org.springframework.http.HttpStatus;

public class EmptyValueException extends AppException {
    public EmptyValueException(String message, String value) {
        super(
                HttpStatus.BAD_REQUEST,
                message,
                value + " cannot be empty",
                "Empty value",
                ExceptionCode.VALUE_IS_REQUIRED
        );
    }
}
