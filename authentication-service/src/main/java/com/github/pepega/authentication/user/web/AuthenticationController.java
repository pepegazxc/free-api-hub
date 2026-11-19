package com.github.pepega.authentication.user.web;

import com.github.pepega.authentication.security.cookie.AuthCookieFactory;
import com.github.pepega.authentication.user.dto.AuthTokens;
import com.github.pepega.authentication.user.dto.RegistrationRequest;
import com.github.pepega.authentication.user.service.AuthService;
import com.github.pepega.authentication.user.service.JwtService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthService authService;
    private final AuthCookieFactory cookie;
    private final JwtService jwtService;

    @PostMapping("/registration")
    public ResponseEntity<Void> registration(@Valid @RequestBody RegistrationRequest request){
        log.info("Registering user: {}", request.username());

        AuthTokens token = authService.register(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .header(HttpHeaders.SET_COOKIE, cookie.accessCookie(token.accessToken()).toString())
                .header(HttpHeaders.SET_COOKIE, cookie.refreshCookie(token.refreshToken()).toString())
                .build();
    }

    @PostMapping("/refresh")
    public ResponseEntity<Void> refreshTokens(
            @CookieValue(name = "refresh_token", required = false) String refreshToken
    ){
        if (refreshToken == null || refreshToken.isBlank()){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        AuthTokens newToken = jwtService.rotateTokens(refreshToken);

        return ResponseEntity.status(HttpStatus.OK)
                .header(HttpHeaders.SET_COOKIE, cookie.accessCookie(newToken.accessToken()).toString())
                .header(HttpHeaders.SET_COOKIE, cookie.refreshCookie(newToken.refreshToken()).toString())
                .build();
    }
}
