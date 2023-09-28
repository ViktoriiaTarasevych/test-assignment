package com.example.testassignment.exception;

public class AgeRestrictionException extends RuntimeException {

    public AgeRestrictionException() {
        super("You do not meet the age requirement.");
    }

    public AgeRestrictionException(String message) {
        super(message);
    }

    public AgeRestrictionException(String message, Throwable cause) {
        super(message, cause);
    }
}
