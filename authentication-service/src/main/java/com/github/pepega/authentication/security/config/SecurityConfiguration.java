package com.github.pepega.authentication.security.config;

import com.github.pepega.authentication.security.jwt.JwtAuthenticationFilter;
import com.github.pepega.authentication.security.jwt.JwtVerifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;import org.springframework.security.web.authentication.HttpStatusEntryPoint;import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableConfigurationProperties(JwtProperties.class)
public class SecurityConfiguration {

    public SecurityFilterChain filterChain(HttpSecurity http,
                                           JwtVerifier jwtVerifier) throws Exception{
        http
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "api/v1/auth/registration"
                        )
                        .permitAll()
                        .anyRequest().authenticated()
                )                .exceptionHandling(e -> e
                        .authenticationEntryPoint(
                                new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
                .addFilterBefore(new JwtAuthenticationFilter(jwtVerifier),
                        UsernamePasswordAuthenticationFilter.class)
                .build();

        return http.build();
    }

    @Bean
    PasswordEncoder passwordEncoder(){
        return new Argon2PasswordEncoder(16, 32, 1, 19 * 1024, 2);
    }
}
