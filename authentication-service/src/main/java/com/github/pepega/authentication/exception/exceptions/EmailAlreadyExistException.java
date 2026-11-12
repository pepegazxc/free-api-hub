package com.github.pepega.authentication.exception.exceptions;

import com.github.pepega.authentication.exception.model.ExceptionCode;
import org.springframework.http.HttpStatus;

public class EmailAlreadyExistException extends AppException{
    public EmailAlreadyExistException() {
        super(
                HttpStatus.CONFLICT,
                "User with this email already exists",
                "Couldn't registered new user because of existed email",
                "Email already exist",
                ExceptionCode.EMAIL_EXIST
        );
    }
}
