package com.github.pepega.authentication.exception.exceptions;

import com.github.pepega.authentication.exception.model.ExceptionCode;
import org.springframework.http.HttpStatus;

public class WrongTokenTypeException extends AppException {
    public WrongTokenTypeException(String message) {
        super(
                HttpStatus.BAD_REQUEST,
                message,
                "Didn't get the correct one token type",
                "Wrong token type",
                ExceptionCode.WRONG_TOKEN_TYPE
        );
    }
}
