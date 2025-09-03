package com.rakshi.bank.gatewayservice.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;

@Component
@RequiredArgsConstructor
public class JwtUtil {

    private final SecretKey key;

    public String extractUsername(Claims claims) {
        return claims.getSubject();
    }

    public String getRoles(Claims claims) {
        return claims.get("roles", String.class);
    }

    private Claims getClaimsFromToken(String token) {
        return Jwts
                .parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public Claims validateToken(String authToken) {
        return this.getClaimsFromToken(authToken);
    }

}
