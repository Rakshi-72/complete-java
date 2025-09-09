package com.rakshi.bank.userservice.exception.handler;

import com.rakshi.bank.domains.dto.commons.ApiStatus;
import com.rakshi.bank.userservice.exception.exceptions.*;
import com.rakshi.bank.userservice.utils.annotations.WrapError;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.stream.Collectors;

@ControllerAdvice
@Slf4j
public class UserExceptionHandler {


    private Object handle(Exception ex, String logMessage) {
        log.error(logMessage, ex);
        return ex.getMessage();
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    @WrapError(error = "User already exists", status = HttpStatus.CONFLICT, apiStatus = ApiStatus.CONFLICT)
    public Object handleUserAlreadyExists(UserAlreadyExistsException ex) {
        return handle(ex, "User already exists");
    }

    @ExceptionHandler(UserRegistrationException.class)
    @WrapError(error = "Registration failed", status = HttpStatus.CONFLICT, apiStatus = ApiStatus.CONFLICT)
    public Object handleUserRegistration(UserRegistrationException ex) {
        return handle(ex, "User registration failed");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @WrapError(error = "Invalid request data")
    public Object handleValidation(MethodArgumentNotValidException ex) {
        log.error("Validation failed", ex);
        BindingResult result = ex.getBindingResult();
        return result.getFieldErrors().stream()
                .collect(Collectors.groupingBy(
                        FieldError::getField,
                        Collectors.mapping(FieldError::getDefaultMessage, Collectors.toList())
                ));
    }

    @ExceptionHandler(NoResourceFoundException.class)
    @WrapError(error = "Resource not found", status = HttpStatus.NOT_FOUND, apiStatus = ApiStatus.NOT_FOUND)
    public Object handleResourceNotFound(NoResourceFoundException ex) {
        return handle(ex, "Resource not found");
    }

    @ExceptionHandler(InvalidIdentity.class)
    @WrapError(error = "Find by identity failed", status = HttpStatus.BAD_REQUEST, apiStatus = ApiStatus.BAD_REQUEST)
    public Object handleInvalidIdentity(InvalidIdentity ex) {
        return handle(ex, "Find by identity failed");
    }

    @ExceptionHandler(UserNotFoundException.class)
    @WrapError(error = "Find by identity failed", status = HttpStatus.NOT_FOUND, apiStatus = ApiStatus.NOT_FOUND)
    public Object handleUserNotFound(UserNotFoundException ex) {
        return handle(ex, "User not found");
    }

    @ExceptionHandler(PermissionNotPassedException.class)
    @WrapError(error = "Find by identity failed", status = HttpStatus.FORBIDDEN, apiStatus = ApiStatus.UNAUTHORIZED)
    public Object handlePermissionNotValid(PermissionNotPassedException ex) {
        return handle(ex, "Permission not satisfied");
    }

    @ExceptionHandler(Exception.class)
    @WrapError(error = "Unhandled exception", apiStatus = ApiStatus.INTERNAL_SERVER_ERROR, status = HttpStatus.INTERNAL_SERVER_ERROR)
    public Object handleGenericException(Exception ex) {
        return handle(ex, "Unhandled exception");
    }

}
