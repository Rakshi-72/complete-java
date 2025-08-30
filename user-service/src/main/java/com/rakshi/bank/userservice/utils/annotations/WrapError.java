package com.rakshi.bank.userservice.utils.annotations;

import com.rakshi.bank.domains.dto.commons.ApiStatus;
import org.springframework.http.HttpStatus;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface WrapError {
    HttpStatus status() default HttpStatus.BAD_REQUEST;

    String error() default "something went wrong";

    ApiStatus apiStatus() default ApiStatus.BAD_REQUEST;

    boolean success() default false;
}

