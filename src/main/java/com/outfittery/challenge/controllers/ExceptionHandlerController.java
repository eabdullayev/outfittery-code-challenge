package com.outfittery.challenge.controllers;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.outfittery.challenge.exceptions.BadRequestException;
import com.outfittery.challenge.exceptions.GenericException;
import com.outfittery.challenge.exceptions.ResourceNotFoundException;
import com.outfittery.challenge.rest.dto.ExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import javax.validation.ConstraintViolationException;
import java.util.List;

/**
 * handles all exception and convert it read friendly format
 */
@RestControllerAdvice
@RestController
public class ExceptionHandlerController {

    /**
     * handles all validation problem in primitive and list type arguments
     * @param ex - exception class
     * @param request - used for retrieve rest path
     * @return
     */
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionResponse constraintViolationHandler(ConstraintViolationException ex, WebRequest request) {
        return new ExceptionResponse(ex.getConstraintViolations(),
                request.getDescription(false));
    }

    /**
     * handles all validation problem for object type arguments
     * @param ex - exception class
     * @param request - used for retrieve rest path
     * @return
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionResponse methodArgsNotValidHandler(MethodArgumentNotValidException ex, WebRequest request) {
        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();

        return new ExceptionResponse(fieldErrors,
                request.getDescription(false));
    }

    @ExceptionHandler(InvalidFormatException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionResponse invalidFormatHandler(InvalidFormatException ex, WebRequest request) {
        return new ExceptionResponse(ex.getMessage(), ex.getValue(),
                request.getDescription(false));
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ExceptionResponse resourceNotFoundHandler(ResourceNotFoundException ex, WebRequest request) {
        return new ExceptionResponse(ex.getMessage(), ex.getRejectedValue(),
                request.getDescription(false));
    }

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionResponse badRequesthandler(BadRequestException ex, WebRequest request) {
        return new ExceptionResponse(ex.getMessage(), ex.getRejectedValue(),
                request.getDescription(false));
    }

    @ExceptionHandler(GenericException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ExceptionResponse genericExceptinHandler(GenericException ex, WebRequest request) {
        return new ExceptionResponse(ex.getMessage(),
                request.getDescription(false));
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ExceptionResponse unhandledExceptions(Exception ex, WebRequest request) {
        return new ExceptionResponse(ex.getMessage(),
                request.getDescription(false));
    }
}
