package com.tes.ebayuserauthservice.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

@Slf4j
@RestControllerAdvice
public class GlobalRestExceptionHandler {

    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoRecordOfAccessTokenException.class)
    public ErrorResponse handleNoRecordOfAccessTokenException(NoRecordOfAccessTokenException exception, WebRequest request) {
        log.warn("NoRecordOfAccessTokenException occurred: " + exception.getMessage());

        return new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                new Date(),
                exception.getMessage(),
                request.getDescription(false)
        );
    }

    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoRecordOfRefreshTokenException.class)
    public ErrorResponse handleNoRecordOfRefreshTokenException(NoRecordOfRefreshTokenException exception, WebRequest request) {
        log.warn("NoRecordOfRefreshTokenException occurred: " + exception.getMessage());

        return new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                new Date(),
                exception.getMessage(),
                request.getDescription(false)
        );
    }

    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoRecordOfAuthCodeException.class)
    public ErrorResponse handleNoRecordOfAuthCodeException(NoRecordOfAuthCodeException exception, WebRequest request) {
        log.warn("NoRecordOfAuthCodeException occurred: " + exception.getMessage());

        return new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                new Date(),
                exception.getMessage(),
                request.getDescription(false)
        );
    }

    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(ExpiredAuthCodeException.class)
    public ErrorResponse handleExpiredAuthCodeException(ExpiredAuthCodeException exception, WebRequest request) {
        log.warn("ExpiredAuthCodeException occurred: " + exception.getMessage());

        return new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                new Date(),
                exception.getMessage(),
                request.getDescription(false)
        );
    }

    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(ExpiredRefreshTokenException.class)
    public ErrorResponse handleExpiredRefreshTokenException(ExpiredRefreshTokenException exception, WebRequest request) {
        log.warn("ExpiredAuthCodeException occurred: " + exception.getMessage());

        return new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                new Date(),
                exception.getMessage(),
                request.getDescription(false)
        );
    }
}
