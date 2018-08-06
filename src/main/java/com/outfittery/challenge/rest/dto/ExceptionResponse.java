package com.outfittery.challenge.rest.dto;

import java.time.LocalDate;

public class ExceptionResponse {
    private LocalDate date;
    private String messate;
    private Object invalidValue;
    private String details;

    public ExceptionResponse(String messate, Object invalidValue, String details) {
        this(LocalDate.now(), messate, invalidValue, details);
    }

    public ExceptionResponse(LocalDate date, String messate, Object invalidValue, String details) {
        this.date = date;
        this.messate = messate;
        this.details = details;
        this.invalidValue = invalidValue;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getMessate() {
        return messate;
    }

    public void setMessate(String messate) {
        this.messate = messate;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public Object getInvalidValue() {
        return invalidValue;
    }

    public void setInvalidValue(Object invalidValue) {
        this.invalidValue = invalidValue;
    }
}
