package com.rakshi.bank.gatewayservice.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.cloud.gateway.support.NotFoundException;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.resource.NoResourceFoundException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Component
@Order(-2)
@Slf4j
public class GlobalErrorHandler implements ErrorWebExceptionHandler {

    private static final Map<HttpStatus, String> ERROR_MESSAGES = new HashMap<>();

    static {
        ERROR_MESSAGES.put(HttpStatus.NOT_FOUND, "The requested resource was not found");
        ERROR_MESSAGES.put(HttpStatus.UNAUTHORIZED, "Authentication required");
        ERROR_MESSAGES.put(HttpStatus.FORBIDDEN, "Access denied");
        ERROR_MESSAGES.put(HttpStatus.INTERNAL_SERVER_ERROR, "An internal server error occurred");
        ERROR_MESSAGES.put(HttpStatus.BAD_REQUEST, "Bad request");
        ERROR_MESSAGES.put(HttpStatus.CONFLICT, "Resource already exists");
        ERROR_MESSAGES.put(HttpStatus.SERVICE_UNAVAILABLE, "Service unavailable");
        ERROR_MESSAGES.put(HttpStatus.NOT_IMPLEMENTED, "Not implemented");
    }

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        log.error("Global error handler caught exception: {}", ex.getClass().getName());
        log.error("Exception message: {}", ex.getMessage());

        ServerHttpResponse response = exchange.getResponse();

        if (response.isCommitted()) {
            return Mono.error(ex);
        }

        HttpStatus status;

        if (ex instanceof NoResourceFoundException) {
            status = HttpStatus.NOT_FOUND;
        } else if (ex instanceof NotFoundException) {
            status = HttpStatus.SERVICE_UNAVAILABLE;
        } else {
            log.error("Unknown exception: {}", ex.getClass().getName());
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        response.setStatusCode(status);
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        String errorMessage = ERROR_MESSAGES.getOrDefault(status, "An error occurred");

        String body = String.format("""
                {
                  "status": %d,
                  "error": "%s",
                  "message": "%s"
                }
                """, status.value(), status.name(), errorMessage);

        byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
        return response.writeWith(Mono.just(response.bufferFactory().wrap(bytes)));
    }
}