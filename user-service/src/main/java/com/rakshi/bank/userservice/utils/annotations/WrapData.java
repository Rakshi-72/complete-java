package com.rakshi.bank.userservice.utils.annotations;

import com.rakshi.bank.domains.dto.commons.ApiStatus;
import org.springframework.http.HttpStatus;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface WrapData {
    HttpStatus status() default HttpStatus.OK;

    String message() default "all good!";

    ApiStatus apiStatus() default ApiStatus.SUCCESS;

    boolean success() default true;
}
