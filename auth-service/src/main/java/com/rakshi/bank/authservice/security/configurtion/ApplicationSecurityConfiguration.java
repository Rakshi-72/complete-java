package com.rakshi.bank.authservice.security.configurtion;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@RequiredArgsConstructor
public class ApplicationSecurityConfiguration {

    private final AuthenticationManager authenticationManager;

    //    TODO: as of now cors is disabled, csrf is disabled, form login is disabled, http basic is disabled
    @Bean
    SecurityFilterChain getSecurityFilterChain(HttpSecurity http) throws Exception {

        http.cors(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable);

        http.authorizeHttpRequests(
                        authz -> authz.requestMatchers("/api/public/**",      // registration, login, open endpoints
                                        "/actuator/health",
                                        "/swagger-ui/**",
                                        "/v3/api-docs/**")
                                .permitAll()
                                .anyRequest()
                                .authenticated()
                )
                .authenticationManager(authenticationManager);

        return http.build();
    }

}
