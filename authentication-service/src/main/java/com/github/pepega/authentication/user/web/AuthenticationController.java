package com.github.pepega.authentication.user.web;

import com.github.pepega.authentication.security.cookie.AuthCookieFactory;
import com.github.pepega.authentication.user.dto.AuthTokens;
import com.github.pepega.authentication.user.dto.RegistrationRequest;
import com.github.pepega.authentication.user.service.AuthService;
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

    @PostMapping("/registration")
    public ResponseEntity<Void> registration(@Valid @RequestBody RegistrationRequest request){
        log.info("Registering user: {}", request);

        AuthTokens token = authService.register(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .header(HttpHeaders.SET_COOKIE, cookie.accessCookie(token.accessToken()).toString())
                .header(HttpHeaders.SET_COOKIE, cookie.refreshCookie(token.refreshToken()).toString())
                .build();
    }
}
