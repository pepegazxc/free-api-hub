package com.github.pepega.authentication.exception.exceptions;

import org.springframework.http.HttpStatus;

public class WrongTokenTypeException extends AppException {
    public WrongTokenTypeException(String message) {
        super(
                HttpStatus.BAD_REQUEST,
                message,
                "Didn't get the correct one token type",
                "Wrong token type",
                "WRONG_TOKEN_TYPE"
        );
    }
}
