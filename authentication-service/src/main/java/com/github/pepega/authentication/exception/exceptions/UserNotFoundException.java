package com.github.pepega.authentication.exception.exceptions;

import com.github.pepega.authentication.exception.model.ExceptionCode;
import org.springframework.http.HttpStatus;

public class UserNotFoundException extends AppException {
    public UserNotFoundException(String userId) {
        super(
                HttpStatus.NOT_FOUND,
                "Couldn't found user. Please try again",
                "Couldn't found user by id=" + userId,
                "User not found",
                ExceptionCode.USER_NOT_FOUND
        );
    }
}
