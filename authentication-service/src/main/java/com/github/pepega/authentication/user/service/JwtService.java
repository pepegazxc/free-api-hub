package com.github.pepega.authentication.user.service;

import com.github.pepega.authentication.security.jwt.JwtIssuer;
import com.github.pepega.authentication.user.dto.AuthTokens;
import com.github.pepega.authentication.user.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class JwtService {

    private final JwtIssuer jwtIssuer;
    private final RedisService redisService;

    public AuthTokens saveTokens(UserEntity user){
        UUID userid = user.getId();
        String accessToken = jwtIssuer.generateAccessToken(userid);
        String refreshToken = jwtIssuer.generateRefreshToken(userid);

        redisService.saveRefreshToken(userid, refreshToken);

        return new AuthTokens(
                accessToken, refreshToken
        );
    }
}
