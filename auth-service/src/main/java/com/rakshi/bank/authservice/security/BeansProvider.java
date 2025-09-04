package com.rakshi.bank.authservice.security;

import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.crypto.SecretKey;
import java.util.Base64;

@Configuration
public class BeansProvider {

    @Value("${jwt.secret}")
    private String JWT_SECRETE;

    @Bean
    SecretKey getKey() {
        byte[] encodedKey = Base64.getEncoder().encode(JWT_SECRETE.getBytes());
        return Keys.hmacShaKeyFor(encodedKey);
    }

    @Bean
    PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
