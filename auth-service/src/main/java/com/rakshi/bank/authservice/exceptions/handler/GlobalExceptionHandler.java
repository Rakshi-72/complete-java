package com.rakshi.bank.authservice.exceptions.handler;

import com.rakshi.bank.authservice.exceptions.CredentialsMissMatchException;
import com.rakshi.bank.authservice.exceptions.UserNotFoundException;
import com.rakshi.bank.domains.dto.commons.ApiStatus;
import com.rakshi.bank.domains.dto.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private ResponseEntity<ApiResponse<String>> buildResponse(HttpStatus status, String message, String error) {
        return ResponseEntity.status(status)
                .body(ApiResponse.<String>builder()
                        .message(message)
                        .error(error)
                        .success(false)
                        .status(ApiStatus.FAILURE)
                        .build());
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiResponse<String>> handleUserNotFound(UserNotFoundException ex) {
        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage(), ex.getMessage());
    }

    @ExceptionHandler(CredentialsMissMatchException.class)
    public ResponseEntity<ApiResponse<String>> handleBadCredentials(CredentialsMissMatchException ex) {
        return buildResponse(HttpStatus.UNAUTHORIZED, ex.getMessage(), ex.getMessage());
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiResponse<String>> handleAuth(AuthenticationException ex) {
        return buildResponse(HttpStatus.UNAUTHORIZED, "Authentication failed", ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        return buildResponse(HttpStatus.BAD_REQUEST, "validation failed", errorMessage);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<String>> handleException(Exception ex) {
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred", ex.getMessage());
    }
}
