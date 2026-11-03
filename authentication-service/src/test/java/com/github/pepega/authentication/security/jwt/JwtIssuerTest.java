package com.github.pepega.authentication.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.*;
import com.github.pepega.authentication.security.config.JwtProperties;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

public class JwtIssuerTest {

    private static KeyPair keyPair;

    private JwtProperties properties;
    private JwtIssuer jwtIssuer;

    @BeforeAll
    static void generateKeys() throws Exception {
        KeyPairGenerator gen = KeyPairGenerator.getInstance("RSA");
        gen.initialize(2048);

        keyPair = gen.generateKeyPair();
    }


    @BeforeEach
    void setUp() {
        String privateKey = keyPair.getPrivate().getFormat();
        String publicKey = keyPair.getPublic().getFormat();
        properties = new JwtProperties("test-issuer", Duration.ofMinutes(15), Duration.ofDays(30), privateKey, publicKey);

        jwtIssuer = new JwtIssuer(keyPair.getPrivate(), properties);
    }

    private Claims parse(String token){
        return Jwts.parser()
                .verifyWith(keyPair.getPublic())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    @Nested
    @DisplayName("HappyPath")
    class SuccessTests{

        @Test
        public void accessToken_shouldGenerateValidIssuer(){
            String token = jwtIssuer.generateAccessToken(1L);

            Claims parsed = parse(token);

            assertEquals( "test-issuer" , parsed.getIssuer(), "The issuer must be equals");
        }

        @Test
        public void accessToken_shouldAddUserId(){
            String token = jwtIssuer.generateAccessToken(1L);

            Claims parsed = parse(token);

            assertEquals("1", parsed.getSubject(), "UserId must be equals");

        }

        @Test
        public void accessToken_shouldAddCorrectClaims(){
            String token = jwtIssuer.generateAccessToken(1L);

            Claims parsed = parse(token);

            String tokenType = parsed.get("token_type", String.class);
            Integer userId = parsed.get("user_id", Integer.class);

            assertEquals("ACCESS_TOKEN", tokenType, "Token type must be equals");
            assertEquals(1, userId, "UserId must be equals");
        }

        @Test
        public void accessToken_shouldAddId(){
            String token = jwtIssuer.generateAccessToken(1L);

            Claims parsed = parse(token);

            String tokenId = parsed.getId();

            assertNotNull(tokenId, "Token must have an id");
        }

        @Test
        public void accessToken_shouldAddCorrectTtl(){
            String token = jwtIssuer.generateAccessToken(1L);

            Claims parsed = parse(token);

            long ttl = (parsed.getExpiration().getTime() - parsed.getIssuedAt().getTime()) / 1000;

            assertEquals(ttl, Duration.ofMinutes(15).toSeconds(), "The ttl must be equals for the access token");
        }

        @Test
        public void refreshToken_shouldAddCorrectTtl(){
            String token = jwtIssuer.generateRefreshToken(1L);

            Claims parsed = parse(token);

            long ttl = (parsed.getExpiration().getTime() - parsed.getIssuedAt().getTime()) / 1000;

            assertEquals(ttl, Duration.ofDays(30).toSeconds(), "The ttl must be equals for the refresh token");
        }

        @Test
        public void refreshToken_shouldAddCorrectClaims(){
            String token = jwtIssuer.generateRefreshToken(1L);

            Claims parsed = parse(token);

            String tokenType = parsed.get("token_type", String.class);
            Integer userId = parsed.get("user_id", Integer.class);

            assertEquals("REFRESH_TOKEN", tokenType, "Token type must be equals");
            assertEquals(1, userId, "UserId must be equals");
        }

        @Test
        public void accessToken_checkUniquenessOfId(){
            String firstToken = jwtIssuer.generateAccessToken(1L);
            String secondToken = jwtIssuer.generateAccessToken(2L);

            Claims firstParsed = parse(firstToken);
            Claims secondParsed = parse(secondToken);

            String firstId = firstParsed.getId();
            String secondId = secondParsed.getId();

            assertNotEquals(firstId, secondId, "The ids  must not be equals");


        }
    }
}
