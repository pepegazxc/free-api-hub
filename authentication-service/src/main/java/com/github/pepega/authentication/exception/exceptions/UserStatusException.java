package com.github.pepega.authentication.exception.exceptions;

import com.github.pepega.authentication.exception.model.ExceptionCode;
import org.springframework.http.HttpStatus;

public class UserStatusException extends AppException{
    public UserStatusException(String message) {
        super(
                HttpStatus.CONFLICT,
                message,
                "User status is ",
                "Incorrect user status",
                ExceptionCode.INCORRECT_USER_STATUS
        );
    }
}
