package com.ssafy.enjoytrip.exception;

public class AuthenticationException extends RuntimeException {
    private static final String message = "인증되지 않은 요청입니다.";

    public AuthenticationException() {
        super(message);
    }

    public AuthenticationException(String message) {
        super(message);
    }
}
