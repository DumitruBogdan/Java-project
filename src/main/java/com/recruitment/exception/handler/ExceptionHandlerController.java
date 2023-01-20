package com.recruitment.exception.handler;

import com.recruitment.exception.EntityAlreadyExistsException;
import com.recruitment.exception.EntityNotFoundException;
import com.recruitment.exception.RequestValidationException;
import com.recruitment.exception.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;


import java.time.LocalDateTime;

@Slf4j
@RestControllerAdvice
public class ExceptionHandlerController {
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler({ResourceNotFoundException.class, EntityNotFoundException.class})
    public ErrorResponse handleEntityNotFoundException(Exception exception) {
        ErrorResponse errorResponse = new ErrorResponse(exception.getMessage(), LocalDateTime.now());
        log.error(exception.getMessage());

        return errorResponse;
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler({EntityAlreadyExistsException.class})
    public ErrorResponse handleEntityAlreadyExistsException(EntityAlreadyExistsException exception) {
        ErrorResponse errorResponse = new ErrorResponse(exception.getMessage(), LocalDateTime.now());
        log.error(exception.getMessage());

        return errorResponse;
    }

    @ResponseStatus(HttpStatus.PRECONDITION_FAILED)
    @ExceptionHandler({RequestValidationException.class})
    public ErrorResponse handleRequestValidationException(RequestValidationException exception) {
        ErrorResponse errorResponse = new ErrorResponse(exception.getMessage(), LocalDateTime.now());
        log.error(exception.getMessage());

        return errorResponse;
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ErrorResponse serverError(Exception exception) {
        ErrorResponse errorResponse = new ErrorResponse(exception.getMessage(), LocalDateTime.now());
        log.error("Server error: {}", exception.getMessage());

        return errorResponse;
    }
}

