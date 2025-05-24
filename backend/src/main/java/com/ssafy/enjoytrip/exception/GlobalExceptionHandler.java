package com.ssafy.enjoytrip.exception;

import com.ssafy.enjoytrip.features.auth.application.exception.UserValidationFailedException;
import com.ssafy.enjoytrip.features.user.application.exception.UserNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {
    private ResponseEntity<String> buildErrorResponseEntity(HttpStatus status, RuntimeException ex) {
        log.error("{}", ex.getMessage(), ex);
        return ResponseEntity.status(status).body(ex.getMessage());
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<String> handleUnauthorizedException(RuntimeException ex) {
        return buildErrorResponseEntity(HttpStatus.UNAUTHORIZED, ex);
    }

    @ExceptionHandler({SQLException.class, UserNotFoundException.class, UserValidationFailedException.class})
    public ResponseEntity<String> handleBadRequestException(RuntimeException ex) {
        return buildErrorResponseEntity(HttpStatus.BAD_REQUEST, ex);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(RuntimeException ex) {
        return buildErrorResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, ex);
    }
}
