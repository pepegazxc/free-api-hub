package com.github.pepega.authentication.exception.exceptions;

import com.github.pepega.authentication.exception.model.ExceptionCode;
import org.springframework.http.HttpStatus;

public class UsernameAlreadyExistException extends AppException {
    public UsernameAlreadyExistException(String username) {
        super(
                HttpStatus.CONFLICT,
                "User with this username already exists",
                "User with username: '"+ username + "' already exist",
                "Username taken",
                ExceptionCode.USERNAME_TAKEN
        );
    }
}
