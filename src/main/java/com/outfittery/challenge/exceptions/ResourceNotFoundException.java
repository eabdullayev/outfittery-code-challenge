package com.outfittery.challenge.exceptions;

/**
 * Used for data not found in DB
 */
public class ResourceNotFoundException extends RuntimeException {

    private Object rejectedValue;

    public ResourceNotFoundException() {
    }

    public ResourceNotFoundException(String message, Object rejectedValue) {
        super(message);
        this.rejectedValue = rejectedValue;
    }

    public Object getRejectedValue() {
        return rejectedValue;
    }
}
