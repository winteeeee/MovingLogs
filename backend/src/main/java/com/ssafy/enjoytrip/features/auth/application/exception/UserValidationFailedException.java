package com.ssafy.enjoytrip.features.auth.application.exception;

public class UserValidationFailedException extends RuntimeException {
    public UserValidationFailedException(String message) {
        super(message);
    }
}
