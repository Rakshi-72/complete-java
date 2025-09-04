package com.rakshi.bank.gatewayservice.constants;

import java.util.Arrays;
import java.util.List;

public class ApplicationConstants {
    public static final List<String> PUBLIC_ENDPOINTS = Arrays.asList(
            "/api/public/**",
            "/actuator/health",
            "/swagger-ui",
            "/v3/api-docs"
    );
}
