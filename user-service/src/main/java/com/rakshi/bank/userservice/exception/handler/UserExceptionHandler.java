package com.rakshi.bank.userservice.exception.handler;

import com.rakshi.bank.domains.dto.commons.ApiStatus;
import com.rakshi.bank.userservice.exception.exceptions.UserAlreadyExistsException;
import com.rakshi.bank.userservice.exception.exceptions.UserRegistrationException;
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

    @ExceptionHandler(UserAlreadyExistsException.class)
    @WrapError(error = "User already exists", status = HttpStatus.CONFLICT, apiStatus = ApiStatus.CONFLICT)
    public Object handleUserAlreadyExistsException(UserAlreadyExistsException exception) {
        log.error("User already exists");
        exception.printStackTrace();
        return exception.getMessage();
    }

    @WrapError(error = "Registration failed", status = HttpStatus.CONFLICT, apiStatus = ApiStatus.CONFLICT)
    @ExceptionHandler(UserRegistrationException.class)
    public Object handleUserRegistrationException(UserRegistrationException exception) {
        exception.printStackTrace();
        log.error("User registration failed");
        return exception.getMessage();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @WrapError(error = "invalid request data")
    public Object handleValidationException(MethodArgumentNotValidException exception) {
        exception.printStackTrace();
        log.error("Validation failed");
        BindingResult result = exception.getBindingResult();
        return result.getFieldErrors().stream()
                .collect(Collectors.groupingBy(FieldError::getField,
                        Collectors.mapping(FieldError::getDefaultMessage, Collectors.toList())));
    }

    @ExceptionHandler(NoResourceFoundException.class)
    @WrapError(error = "Resource not found", status = HttpStatus.NOT_FOUND, apiStatus = ApiStatus.NOT_FOUND)
    public Object handleResourceNotFoundException(NoResourceFoundException exception) {
        exception.printStackTrace();
        log.error("Resource not found");
        return exception.getMessage();
    }

    @ExceptionHandler(Exception.class)
    @WrapError(error = "Unhandled exception", apiStatus = ApiStatus.INTERNAL_SERVER_ERROR, status = HttpStatus.INTERNAL_SERVER_ERROR)
    public Object handleException(Exception exception) {
        exception.printStackTrace();
        log.error("Unhandled exception");
        return exception.getMessage();
    }

}
