package com.chefstable.backend.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

    private final SecretKey secretKey;
    private final Duration expiration;
    private final Duration refreshExpiration;

    public JwtService(
            @Value("${app.jwt.secret}") String secret,
            @Value("${app.jwt.expiration-minutes}") long expirationMinutes,
            @Value("${app.jwt.refresh-expiration-days}") long refreshExpirationDays
    ) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.expiration = Duration.ofMinutes(expirationMinutes);
        this.refreshExpiration = Duration.ofDays(refreshExpirationDays);
    }

    public String generateToken(String clientId) {
        Instant now = Instant.now();
        return Jwts.builder()
                .subject(clientId)
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plus(expiration)))
                .signWith(secretKey)
                .compact();
    }

    public String generateRefreshToken(String clientId) {
        Instant now = Instant.now();
        return Jwts.builder()
                .subject(clientId)
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plus(refreshExpiration)))
                .signWith(secretKey)
                .compact();
    }

    public String parseClientId(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }
}
