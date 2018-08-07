package com.outfittery.challenge.controllers;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.outfittery.challenge.rest.dto.ExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.HashSet;
import java.util.List;

@RestControllerAdvice
@RestController
public class ExceptionHandlerController {

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionResponse constraintViolationHandler(ConstraintViolationException ex, WebRequest request) {
        return new ExceptionResponse(ex.getConstraintViolations(),
                request.getDescription(false));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionResponse methodArgsNotValidHandler(MethodArgumentNotValidException ex, WebRequest request) {
        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();

        return new ExceptionResponse(fieldErrors,
                request.getDescription(false));
    }

    @ExceptionHandler(InvalidFormatException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionResponse methodArgsNotValidHandler(InvalidFormatException ex, WebRequest request) {
        return new ExceptionResponse(ex.getMessage(), ex.getValue(),
                request.getDescription(false));
    }
}
