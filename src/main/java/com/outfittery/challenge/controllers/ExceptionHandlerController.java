package com.outfittery.challenge.controllers;

import com.outfittery.challenge.rest.dto.ExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import javax.validation.ConstraintViolationException;

@RestControllerAdvice
@RestController
public class ExceptionHandlerController {

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionResponse constraintViolationHandler(ConstraintViolationException ex, WebRequest request) {
        return new ExceptionResponse(ex.getConstraintViolations().iterator().next().getMessage(),
                ex.getConstraintViolations().iterator().next().getInvalidValue(),
                request.getDescription(false));
    }
}
