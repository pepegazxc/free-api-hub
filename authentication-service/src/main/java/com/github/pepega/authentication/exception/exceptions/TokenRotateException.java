package com.github.pepega.authentication.exception.exceptions;

import com.github.pepega.authentication.exception.model.ExceptionCode;
import org.springframework.http.HttpStatus;

public class TokenRotateException extends AppException {
    public TokenRotateException() {
        super(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Couldn't refresh tokens",
                "Something went wrong while rotating jwt tokens",
                "Couldn't refresh tokens",
                ExceptionCode.COULDNT_REFRESH_TOKENS
        );
    }
}
