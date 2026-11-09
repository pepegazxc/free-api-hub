package com.github.pepega.authentication.exception.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public abstract class AppException extends RuntimeException {
    private HttpStatus status;
    private String publicMessage;

    protected AppException(HttpStatus status, String publicMessage) {
        this.publicMessage = publicMessage;
        this.status = status;
    }

    protected AppException(Throwable cause, HttpStatus status, String publicMessage) {
        super(publicMessage, cause);
        this.status = status;
    }
}
