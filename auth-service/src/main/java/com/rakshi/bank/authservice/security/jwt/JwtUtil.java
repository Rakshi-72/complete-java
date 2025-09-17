package com.rakshi.bank.authservice.security.jwt;

import com.rakshi.bank.authservice.service.UserService;
import com.rakshi.bank.domains.dto.request.LoginRequest;
import com.rakshi.bank.domains.enums.Roles;
import com.rakshi.bank.domains.models.Role;
import com.rakshi.bank.domains.models.User;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.sql.Date;
import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtUtil {

    private final SecretKey key;
    private final UserService userService;

    @Value("${jwt.expiration}")
    private Integer EXPIRATION_TIME;

    @Value("${jwt.issuer}")
    private String ISSUER;

    public String generateJwtToken(String username, LoginRequest loginRequest) {
        Map<String, Object> claims = new HashMap<>();

        Instant now = Instant.now();
        Duration duration = Duration.ofMinutes(EXPIRATION_TIME);

        User user = userService.findUserByIdentifier(username);
        String roles = user
                .getRoles()
                .stream()
                .map(Role::getRole)
                .map(Roles::toString)
                .collect(Collectors.joining("\\|"));

        log.info("Generating JWT token for user: {}", username);
        return Jwts.builder()
                .claims(claims)
                .subject(username)
                .claim("user_id", user.getUserId())
                .issuer(ISSUER)
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plus(duration)))
                .claim("roles", roles)
                .signWith(key)
                .compact();
    }
}
