package com.github.pepega.authentication.user.controller;

import com.github.pepega.authentication.user.dto.RegistrationRequest;
import com.github.pepega.authentication.user.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@Slf4j
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthService authService;

    @GetMapping("/registration")
    public String returnRegistrationPage(){
        return "register";
    }

    @PostMapping("/registration")
    public String registration(@Valid @ModelAttribute RegistrationRequest request,
                               BindingResult result){
        log.info("Registering user: {}", request);

        if (result.hasErrors()){
            return "index";
        }
        authService.addNewUser(request);
        return "redirect:/index";
    }
}
