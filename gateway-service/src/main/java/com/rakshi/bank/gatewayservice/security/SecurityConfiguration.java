package com.rakshi.bank.gatewayservice.security;

import com.rakshi.bank.gatewayservice.security.filter.ApplicationEntryPointFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;

import static com.rakshi.bank.gatewayservice.constants.ApplicationConstants.PUBLIC_ENDPOINTS;

@Configuration
@EnableWebFluxSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final ApplicationEntryPointFilter jwtAuthenticationWebFilter;

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        return http
                // disable defaults
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
                .logout(ServerHttpSecurity.LogoutSpec::disable)

                // stateless
                .securityContextRepository(NoOpServerSecurityContextRepository.getInstance())

                // authorization rules
                .authorizeExchange(exchanges -> exchanges
                        .pathMatchers(PUBLIC_ENDPOINTS.toArray(new String[0])).permitAll()
                        .anyExchange().authenticated()
                )

                // add our JWT filter
                .addFilterAt(jwtAuthenticationWebFilter, SecurityWebFiltersOrder.AUTHENTICATION)

                .build();
    }
}
