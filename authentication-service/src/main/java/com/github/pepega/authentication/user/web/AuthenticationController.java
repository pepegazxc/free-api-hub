package com.github.pepega.authentication.user.web;

import com.github.pepega.authentication.user.dto.RegistrationRequest;
import com.github.pepega.authentication.user.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthService authService;

    @PostMapping("/registration")
    public String registration(@Valid @RequestBody RegistrationRequest request){
        log.info("Registering user: {}", request);

        authService.addNewUser(request);
        return "";
    }
}
