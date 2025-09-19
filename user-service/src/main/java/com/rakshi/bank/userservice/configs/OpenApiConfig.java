package com.rakshi.bank.userservice.configs;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Value("${server.port:8084}")
    private String serverPort;

    @Value("${server.ssl.enabled:false}")
    private boolean sslEnabled;

    @Bean
    public OpenAPI userServiceOpenAPI() {
        String protocol = sslEnabled ? "https" : "http";

        return new OpenAPI()
                .info(new Info()
                        .title("User Service API")
                        .description("""
                                Banking User Management Service with mTLS Security
                                
                                **Security Notes:**
                                - This service uses mutual TLS (mTLS) for service-to-service communication
                                - All requests must be authenticated through the API Gateway
                                - Client certificates are required for direct service access
                                """)
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Banking Team")
                                .email("rakshijr72@gmail.com")));
//                if you want to use different servers for local development and production
//                .servers(List.of(
//                        new Server()
//                                .url(protocol + "://localhost:" + serverPort)
//                                .description("Local Development Server (mTLS)"),
//                        new Server()
//                                .url("https://api-gateway.banking.com")
//                                .description("Production API Gateway")))

// Uncomment the following section to enable JWT-based authentication.
// This configuration adds both Gateway-JWT and mTLS Certificate security schemes to OpenAPI documentation.

//                .addSecurityItem(new SecurityRequirement()
//                        .addList("Gateway-JWT")
//                        .addList("mTLS-Certificate"))
//                .components(new Components()
//                        .addSecuritySchemes("Gateway-JWT",
//                                new SecurityScheme()
//                                        .type(SecurityScheme.Type.HTTP)
//                                        .scheme("bearer")
//                                        .bearerFormat("JWT")
//                                        .description("JWT token passed through API Gateway"))
//                        .addSecuritySchemes("mTLS-Certificate",
//                                new SecurityScheme()
//                                        .type(SecurityScheme.Type.MUTUALTLS)
//                                        .description("Mutual TLS client certificate authentication")));
    }
}