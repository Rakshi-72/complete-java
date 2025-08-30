package com.rakshi.bank.userservice.utils.aop;

import com.rakshi.bank.domains.dto.response.ApiResponse;
import com.rakshi.bank.userservice.utils.annotations.WrapError;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@Aspect
@Slf4j
public class ErrorWrapper {

    @Around("@annotation(wrapError)")
    public Object wrapExceptionResponse(ProceedingJoinPoint joinPoint, WrapError wrapError) throws Throwable {
        Object result = joinPoint.proceed();

        ApiResponse<Object> response = ApiResponse.builder()
                .success(wrapError.success())
                .error(wrapError.error())
                .data(result)
                .status(wrapError.apiStatus())
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(wrapError.status()).body(response);
    }

}
