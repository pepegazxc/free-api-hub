package com.github.pepega.authentication.security.jwt;

import com.github.pepega.authentication.security.model.TokenType;
import com.github.pepega.authentication.security.config.JwtProperties;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.security.PrivateKey;
import java.time.Duration;
import java.util.Date;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class JwtIssuer {

    private final PrivateKey privateKey;
    private final JwtProperties properties;

    public String generateAccessToken(Long userId){
        return buildToken(userId, TokenType.ACCESS_TOKEN, properties.accessTtl());
    }

    public String generateRefreshToken(Long userId){
        return buildToken(userId, TokenType.REFRESH_TOKEN, properties.refreshTtl());
    }

    private String buildToken(Long userId, TokenType tokenType, Duration ttl){
        Instant now = Instant.now();
        return Jwts.builder()
                .header().and().issuer(properties.issuer())
                .subject(userId.toString())
                .claims(Map.of(
                        TokenType.CLAIM, tokenType,
                        "user_id", userId
                ))
                .id(UUID.randomUUID().toString())
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plus(ttl)))
                .signWith(privateKey, Jwts.SIG.RS256)
                .compact();
    }
}
