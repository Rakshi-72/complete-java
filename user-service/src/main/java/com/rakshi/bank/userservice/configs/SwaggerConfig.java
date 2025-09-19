package com.rakshi.bank.userservice.configs;

import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.responses.ApiResponse;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.HandlerMethod;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenApiCustomizer openApiCustomizer() {
        return openApi -> {
            // Append global security notice
            openApi.getInfo().setDescription(
                    openApi.getInfo().getDescription() + "\n\n" +
                            "**Important:** When testing via Swagger UI, ensure your browser " +
                            "has the client certificate installed for mTLS authentication."
            );
        };
    }

    @Bean
    public OperationCustomizer operationCustomizer() {
        return (Operation operation, HandlerMethod handlerMethod) -> {
            // Add common mTLS error codes
            operation.getResponses().addApiResponse("495",
                    new ApiResponse()
                            .description("SSL Certificate Required - Client certificate missing or invalid"));
            operation.getResponses().addApiResponse("496",
                    new ApiResponse()
                            .description("SSL Certificate Invalid - Client certificate verification failed"));
            return operation;
        };
    }
}
