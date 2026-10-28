package com.github.pepega.authentication.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/registration").permitAll()
                        .anyRequest().authenticated()
                )
                .build();

        return http.build();
    }

    @Bean
    PasswordEncoder passwordEncoder(){
        return new Argon2PasswordEncoder(16, 32, 1, 19 * 1024, 2);
    }
}
