package com.outfittery.challenge.exceptions;

public class BadRequestException extends RuntimeException {
    private Object rejectedValue;
    public BadRequestException() {
    }

    public BadRequestException(String message, Object rejectedValue) {
        super(message);
        this.rejectedValue = rejectedValue;
    }

    public Object getRejectedValue() {
        return rejectedValue;
    }
}
