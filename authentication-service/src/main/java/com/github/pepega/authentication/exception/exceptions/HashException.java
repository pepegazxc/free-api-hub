package com.github.pepega.authentication.exception.exceptions;

import org.springframework.http.HttpStatus;

public class HashException extends AppException {
    public HashException(Throwable e) {
        super(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "An error occurred while hashing"
        );
    }
}
