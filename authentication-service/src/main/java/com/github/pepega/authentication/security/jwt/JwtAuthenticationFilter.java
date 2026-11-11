package com.github.pepega.authentication.security.jwt;

import com.github.pepega.authentication.security.model.TokenPayload;
import com.github.pepega.authentication.security.model.TokenType;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.InvalidBearerTokenException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

import org.springframework.http.HttpHeaders;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private static final String BEARER_PREFIX="Bearer ";

    private final JwtVerifier jwtVerifier;


    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {
        String token = extractToken(request);

        if (token != null){
            try {
                TokenPayload payload = jwtVerifier.parse(token, TokenType.ACCESS_TOKEN);

                var authentication = new UsernamePasswordAuthenticationToken(
                        payload.userId(), null, List.of()
                );
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }catch (InvalidBearerTokenException e){
                log.warn("Authentication failed: Invalid token type or schema. Expected 'Bearer'. Id={}, URI={}", request.getRequestId(), request.getRequestURI());
                SecurityContextHolder.clearContext();
            }
        }

        chain.doFilter(request, response);
    }

    private String extractToken(HttpServletRequest request){
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (header != null && header.startsWith(BEARER_PREFIX)){
            return header.substring(BEARER_PREFIX.length());
        }
        return null;
    }
}
