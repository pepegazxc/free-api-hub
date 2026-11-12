package com.github.pepega.authentication.exception.handler;

import com.github.pepega.authentication.exception.exceptions.AppException;
import com.github.pepega.authentication.exception.model.ConstraintError;
import com.github.pepega.authentication.exception.model.ExceptionCode;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static com.github.pepega.authentication.exception.model.ConstraintError.CONSTRAINT_ERROR;

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
        return buildProblemDetail(e.getStatus(), e.getTitle(), e.getPublicMessage(), e.getCode());
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleUnexpectedException(Exception e, HttpServletRequest request){
        log.error("An unexpected exception occurred: request method={}, request URI={}, message={}",
                request.getMethod(), request.getRequestURI() ,e.getMessage(), e);

        return buildProblemDetail(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error", "An unexpected error occurred. Try again later", ExceptionCode.UNEXPECTED_ERROR);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ProblemDetail handleDataIntegrityException(DataIntegrityViolationException e){
        if (e.getCause() instanceof ConstraintViolationException ex) {
            if (ex.getConstraintName() != null) {
                String constraintName = ex.getConstraintName();

                ConstraintError error = CONSTRAINT_ERROR.get(constraintName);

                if (error != null) {
                    log.warn("Couldn't register new user because of {}. Message={}", error.logContext(), e.getMessage());
                    return buildProblemDetail(error.status(), error.title(), error.detail(), error.code());
                }
            }else{
                log.warn("Constraint violation without constraint name. Message={}",e.getMessage(), e);
            }
        }else{
            log.warn("Database integrity error occurred. Message={}",e.getMessage(), e);
        }

        log.error("Unexpected database integrity error. Message={}",e.getMessage(), e);
        return buildProblemDetail(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Unexpected error",
                "An unexpected exception occurred. Try again later",
                ExceptionCode.UNEXPECTED_ERROR
        );
    }

    private ProblemDetail buildProblemDetail(HttpStatus status, String title, String detail, ExceptionCode code) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(status, detail);
        problemDetail.setTitle(title);
        problemDetail.setProperty("code", code.name());
        return problemDetail;
    }
}
