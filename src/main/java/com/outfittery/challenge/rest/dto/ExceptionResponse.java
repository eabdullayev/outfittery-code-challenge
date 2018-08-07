package com.outfittery.challenge.rest.dto;

import javax.validation.ConstraintViolation;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class ExceptionResponse {
    private LocalDate date;
    List<FieldError> errors = new ArrayList<>();
    private String details;

    public ExceptionResponse(String messate, String details) {
        this(messate, null, details);
    }

    public ExceptionResponse(String messate, Object invalidValue, String details) {
        this(LocalDate.now(), messate, invalidValue, details);
    }

    public ExceptionResponse(LocalDate date, String messate, Object invalidValue, String details) {
        errors.add(new FieldError(null, invalidValue, messate));
        this.date = date;
        this.details = details;
    }

    public ExceptionResponse(List<org.springframework.validation.FieldError> fieldErrors, String details) {
        this.errors = fieldErrors.stream().map(FieldError::new).collect(Collectors.toList());
        this.details = details;
        this.date = LocalDate.now();
    }

    public ExceptionResponse(Set<ConstraintViolation<?>> fieldErrors, String details) {
        this.errors = fieldErrors.stream().map(FieldError::new).collect(Collectors.toList());
        this.details = details;
        this.date = LocalDate.now();
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public List<FieldError> getErrors() {
        return errors;
    }

    public void setErrors(List<FieldError> errors) {
        this.errors = errors;
    }

    static class FieldError{
        private String field;
        private Object rejectedValue;
        private String message;

        public FieldError(org.springframework.validation.FieldError fieldError) {
            this.field = fieldError.getField();
            this.rejectedValue = fieldError.getRejectedValue();
            this.message = fieldError.getDefaultMessage();
        }

        public FieldError(String field, Object rejectedValue, String message) {
            this.field = field;
            this.rejectedValue = rejectedValue;
            this.message = message;
        }

        public FieldError(ConstraintViolation fieldError) {
            this.field = fieldError.getPropertyPath().toString();
            this.rejectedValue = fieldError.getInvalidValue();
            this.message = fieldError.getMessage();
        }

        public String getField() {
            return field;
        }

        public void setField(String field) {
            this.field = field;
        }

        public Object getRejectedValue() {
            return rejectedValue;
        }

        public void setRejectedValue(Object rejectedValue) {
            this.rejectedValue = rejectedValue;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}
