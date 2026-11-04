package com.github.pepega.authentication.security.jwt;

import com.github.pepega.authentication.security.model.TokenPayload;
import com.github.pepega.authentication.security.model.TokenType;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.*;
import com.github.pepega.authentication.security.config.JwtProperties;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PublicKey;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;


public class JwtVerifierTest {
    private static KeyPair validKeys;
    private static KeyPair foreignKeys;


    private JwtParser parser;
    private PublicKey publicKey;
    private JwtProperties properties;

    private JwtVerifier jwtVerifier;
    private JwtIssuer jwtIssuer;

    @BeforeAll
    static void generateKeys() throws Exception {
        KeyPairGenerator validGen = KeyPairGenerator.getInstance("RSA");
        validGen.initialize(2048);
        KeyPairGenerator foreignGen = KeyPairGenerator.getInstance("RSA");
        foreignGen.initialize(2048);

        validKeys = validGen.generateKeyPair();
        foreignKeys = foreignGen.generateKeyPair();
    }


    @BeforeEach
    void setUp(){
        properties = new JwtProperties("test", Duration.ofMinutes(15), Duration.ofDays(30), null , null);

        jwtVerifier = new JwtVerifier( properties, validKeys.getPublic());
        jwtIssuer = new JwtIssuer(validKeys.getPrivate(), properties);
    }


    public Claims parse(String token) {
        return  Jwts.parser()
                .verifyWith(publicKey)
                .requireIssuer("test")
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private JwtBuilder validToken() {
        Instant now = Instant.now();
        return Jwts.builder()
                .issuer("test")
                .subject("42")
                .claim("token_type", "ACCESS_TOKEN")
                .id(UUID.randomUUID().toString())
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plus(Duration.ofMinutes(15))));
    }

    private String sign(JwtBuilder builder) {
        return builder.signWith(validKeys.getPrivate(), Jwts.SIG.RS256).compact();
    }

    @Test
    public void accessToken_success_shouldCorrectlyVerifyToken() {
        String token = sign(validToken().subject("42").id("fixed-jti"));

        TokenPayload result = jwtVerifier.parse(token, TokenType.ACCESS_TOKEN);

        assertEquals(42L, result.userId());
        assertEquals("fixed-jti", result.jti());
    }

    @Test
    public void refreshToken_success_shouldCorrectlyVerifyToken() {
        String token = sign(validToken()
                .subject("42")
                .id("fixed-jti")
                .claim("token_type", "REFRESH_TOKEN"));

        TokenPayload result = jwtVerifier.parse(token, TokenType.REFRESH_TOKEN);

        assertEquals(42L, result.userId());
        assertEquals("fixed-jti", result.jti());
    }

    @Test
    public void refreshToken_failed_shouldReturnExceptionBecauseOfIncorrectType() {
        String token = sign(validToken()
                .claim("token_type", "REFRESH_TOKEN"));

        assertThrows(IllegalStateException.class,
                () -> jwtVerifier.parse(token, TokenType.ACCESS_TOKEN));
    }

    @Test
    public void accessToken_failed_shouldReturnExceptionBecauseOfExpirationDate() {
        String token = sign(validToken()
                .expiration(Date.from(Instant.now().minusSeconds(60))));

        assertThrows(IllegalStateException.class,
                () -> jwtVerifier.parse(token, TokenType.ACCESS_TOKEN));
    }

    @Test
    public void accessToken_failed_shouldReturnExceptionBecauseOfIncorrectType() {
        String token = sign(validToken());

        assertThrows(IllegalStateException.class,
                () -> jwtVerifier.parse(token, TokenType.REFRESH_TOKEN));
    }
}
