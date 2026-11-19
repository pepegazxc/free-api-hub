package com.github.pepega.authentication.security.cookie;

import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
public class AuthCookieFactory {
    private static final String ACCESS_TOKEN="access_token";
    private static final String REFRESH_TOKEN="refresh_token";
    private static final String REFRESH_TOKEN_PATH="api/v1/auth/refresh";
    private static final String ACCESS_TOKEN_PATH="/";

    public ResponseCookie accessCookie(String token){
        return buildCookie(ACCESS_TOKEN, token, Duration.ofMinutes(15), "Lax", ACCESS_TOKEN_PATH);
    }

    public ResponseCookie refreshCookie(String token){
        return buildCookie(REFRESH_TOKEN, token, Duration.ofDays(30), "Strict", REFRESH_TOKEN_PATH);
    }

    public ResponseCookie clearAccess(){
        return buildCookie(ACCESS_TOKEN, "", Duration.ZERO, "Lax", ACCESS_TOKEN_PATH);
    }

    public ResponseCookie clearRefresh(){
        return buildCookie(REFRESH_TOKEN, "", Duration.ZERO, "Strict", REFRESH_TOKEN_PATH);
    }


    private ResponseCookie buildCookie(String tokenType, String token, Duration duration, String site, String path){
        return ResponseCookie.from(tokenType, token)
                .httpOnly(true)
                .secure(true)
                .maxAge(duration)
                .sameSite(site)
                .path(path)
                .build();
    }
}
