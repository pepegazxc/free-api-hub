package com.github.pepega.authentication.exception.exceptions;

import com.github.pepega.authentication.exception.model.ExceptionCode;
import org.springframework.http.HttpStatus;

public class RefreshTokenAlreadySavedException extends AppException{
    public RefreshTokenAlreadySavedException() {
        super(
                HttpStatus.CONFLICT,
                "Can't login you because of existing session. If you want to login again, please logout first",
                "User tried login while existing session",
                "Can't login",
                ExceptionCode.CANT_LOGIN

        );
    }
}
