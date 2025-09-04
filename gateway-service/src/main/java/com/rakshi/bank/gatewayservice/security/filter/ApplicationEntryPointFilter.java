package com.rakshi.bank.gatewayservice.security.filter;

import com.rakshi.bank.gatewayservice.security.jwt.JwtUtil;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

import static com.rakshi.bank.gatewayservice.constants.ApplicationConstants.PUBLIC_ENDPOINTS;

@Component
@RequiredArgsConstructor
@Slf4j
public class ApplicationEntryPointFilter implements WebFilter {

    private static final String BEARER_PREFIX = "Bearer ";
    private static final String USERNAME_HEADER = "x-username";
    private static final String ROLES_HEADER = "x-roles";
    private static final String ERROR_JSON_FORMAT = "{\"error\": \"%s\"}";

    private final JwtUtil jwtUtil;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();
        log.debug("Processing request for path: {}", path);

        if (isPublicEndpoint(path) || !path.startsWith("/api")) {
            log.info("Public or non-API endpoint, skipping authentication: {}", path);
            return chain.filter(exchange);
        }

        log.info("Requiring authentication for API path: {}", path);
        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        String jwt = extractToken(authHeader);

        if (jwt == null) {
            return sendError(exchange, "Authentication required", HttpStatus.UNAUTHORIZED);
        }

        try {
            Claims claims = jwtUtil.validateToken(jwt);
            if (claims == null) {
                return sendError(exchange, "Invalid JWT token", HttpStatus.UNAUTHORIZED);
            }

            // Mutate request to add custom headers
            ServerWebExchange mutatedExchange = exchange.mutate()
                    .request(builder -> builder
                            .header(USERNAME_HEADER, jwtUtil.extractUsername(claims))
                            .header(ROLES_HEADER, jwtUtil.getRoles(claims)))
                    .build();

            return chain.filter(mutatedExchange);

        } catch (ExpiredJwtException e) {
            return sendError(exchange, "Expired JWT", HttpStatus.UNAUTHORIZED);
        } catch (MalformedJwtException e) {
            return sendError(exchange, "Malformed JWT", HttpStatus.UNAUTHORIZED);
        } catch (SignatureException e) {
            return sendError(exchange, "Invalid JWT signature", HttpStatus.UNAUTHORIZED);
        } catch (UnsupportedJwtException e) {
            return sendError(exchange, "Unsupported JWT", HttpStatus.UNAUTHORIZED);
        } catch (IllegalArgumentException e) {
            return sendError(exchange, "JWT claims string is empty", HttpStatus.UNAUTHORIZED);
        } catch (JwtException e) {
            return sendError(exchange, "Invalid JWT token", HttpStatus.UNAUTHORIZED);
        }
    }

    private boolean isPublicEndpoint(String requestUri) {
        AntPathMatcher pathMatcher = new AntPathMatcher();
        return PUBLIC_ENDPOINTS.stream().anyMatch(pattern -> pathMatcher.match(pattern, requestUri));
    }

    private String extractToken(String header) {
        if (header != null && header.startsWith(BEARER_PREFIX)) {
            return header.substring(BEARER_PREFIX.length());
        }
        return null;
    }

    private Mono<Void> sendError(ServerWebExchange exchange, String message, HttpStatus status) {
        exchange.getResponse().setStatusCode(status);
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
        byte[] bytes = String.format(ERROR_JSON_FORMAT, message).getBytes(StandardCharsets.UTF_8);
        return exchange.getResponse().writeWith(Mono.just(exchange.getResponse()
                .bufferFactory().wrap(bytes)));
    }
}
