package com.github.pepega.authentication.user.dto;

import jakarta.validation.constraints.*;

public record  RegistrationRequest (
        @NotBlank(message = "Username is required")
        @Size(min = 3, max = 50, message = "Username must be 3-50 characters")
        @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "Only letters, digits and underscore")
         String username,

        @NotBlank(message = "Email is required")
        @Email(message = "Invalid email")
        @Size(max = 254, message = "Email is too long")
        String email,

        @NotBlank(message = "Password is required")
        @Size(min = 8, max = 72, message = "Password must be 8-72 characters")
        String password
) {

    @Override
    public String toString(){
        return "Request for registration from user " + username;
    }
}
