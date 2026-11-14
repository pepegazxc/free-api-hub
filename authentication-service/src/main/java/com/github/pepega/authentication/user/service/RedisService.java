package com.github.pepega.authentication.user.service;

import com.github.pepega.authentication.exception.exceptions.RefreshTokenAlreadySavedException;
import com.github.pepega.authentication.security.config.JwtProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class RedisService {
    private static final String REFRESH_TOKEN_PREFIX="refresh_token:";

    private final RedisTemplate<String, Object> redisTemplate;
    private final JwtProperties jwtProperties;
    private final HashService hashService;

    public void saveRefreshToken(UUID userId, String token){
        String key = REFRESH_TOKEN_PREFIX + userId;
        byte[] hashedToken = hashService.hash(token);

        Boolean saved = redisTemplate.opsForValue()
                .setIfAbsent(key, hashedToken, jwtProperties.refreshTtl());
        if(Boolean.FALSE.equals(saved)) throw new RefreshTokenAlreadySavedException();

        log.info("Saved refresh token userId={}", userId);
    }
}
