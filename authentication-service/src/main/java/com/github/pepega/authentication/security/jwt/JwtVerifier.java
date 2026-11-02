package com.github.pepega.authentication.security.jwt;

import com.github.pepega.authentication.security.model.TokenPayload;
import com.github.pepega.authentication.security.model.TokenType;
import com.github.pepega.authentication.security.config.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Component;

import java.security.PublicKey;

@Component
public class JwtVerifier {

    private final JwtParser parser;

    public JwtVerifier(JwtProperties properties, PublicKey publicKey) {
        this.parser = Jwts.parser()
                .verifyWith(publicKey)
                .requireIssuer(properties.issuer())
                .build();
    }


    public TokenPayload parse(String token, TokenType expectedType){
        try{
            Claims claims = parser.parseSignedClaims(token).getPayload();

            String type = claims.get(TokenType.CLAIM, String.class);
            if (!expectedType.name().equals(type)){
                throw new IllegalStateException("Wrong token type");
            }
            if (claims.getExpiration() == null || claims.getId() == null) {
                throw new IllegalStateException("Missing exp or jti");
            }

            return new TokenPayload(
                    Long.valueOf(claims.getSubject()),
                    expectedType,
                    claims.getId()
            );
        } catch (JwtException | IllegalArgumentException e){
            throw new IllegalStateException("Invalid token", e);
        }
    }


}
