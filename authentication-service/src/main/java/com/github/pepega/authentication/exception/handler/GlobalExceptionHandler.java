package com.github.pepega.authentication.exception.handler;

import com.github.pepega.authentication.exception.exceptions.AppException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(AppException.class)
    public ProblemDetail handleAppException(AppException e){
        if (e.getStatus().is5xxServerError()){
            log.error("Internal server error occurred. Message={}",
                    e.getMessage(), e);
        }else{
            log.warn("An exception occurred. Status={}, message={}",
                    e.getStatus(), e.getMessage());
        }
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(e.getStatus(), e.getPublicMessage());
        problem.setTitle(e.getTitle());
        problem.setProperty("code", e.getCode());
        return problem;
    }
}
