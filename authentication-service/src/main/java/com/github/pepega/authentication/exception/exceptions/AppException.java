package com.github.pepega.authentication.exception.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public abstract class AppException extends RuntimeException {
    private final HttpStatus status;
    private final String publicMessage;
    private final String title;
    private final String code;


    protected AppException(HttpStatus status, String publicMessage, String message, String title, String code) {
        super(message);
        this.status = status;
        this.publicMessage = publicMessage;
        this.title = title;
        this.code = code;
    }

    protected AppException(HttpStatus status, String publicMessage, String message, String title, String code, Throwable e) {
        super(message, e);
        this.status = status;
        this.publicMessage = publicMessage;
        this.title = title;
        this.code = code;
    }
}
