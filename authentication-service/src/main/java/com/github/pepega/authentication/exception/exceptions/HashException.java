package com.github.pepega.authentication.exception.exceptions;

import com.github.pepega.authentication.exception.model.ExceptionCode;
import org.springframework.http.HttpStatus;

public class HashException extends AppException {
    public HashException(Throwable e, String message) {
        super(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "An error occurred while hashing",
                message,
                "Hashing error",
                ExceptionCode.HASHING_ERROR
        );
    }
}
