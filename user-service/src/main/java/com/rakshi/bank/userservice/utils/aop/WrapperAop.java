package com.rakshi.bank.userservice.utils.aop;

import com.rakshi.bank.domains.dto.response.ApiResponse;
import com.rakshi.bank.userservice.utils.annotations.WrapData;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.LocalDateTime;

@Component
@Aspect
@Slf4j
public class WrapperAop {

    @Around("@annotation(com.rakshi.bank.userservice.utils.annotations.WrapData)")
    public Object wrapResponse(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        WrapData annotation = method.getAnnotation(WrapData.class);

        Object result = joinPoint.proceed();

        ApiResponse<Object> response = ApiResponse
                .builder()
                .data(result)
                .message(annotation.message())
                .success(annotation.success())
//                .status(annotation.apiStatus())
                .timestamp(LocalDateTime.now())
                .build();

        log.debug("Wrapping response for method {}: {}", method.getName(), response);

        return ResponseEntity.status(annotation.status()).body(response);
    }

}
