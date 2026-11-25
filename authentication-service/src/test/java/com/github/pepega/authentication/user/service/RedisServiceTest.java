package com.github.pepega.authentication.user.service;

import com.github.pepega.authentication.exception.exceptions.RefreshTokenAlreadySavedException;
import com.github.pepega.authentication.security.service.HashService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import com.github.pepega.authentication.security.config.JwtProperties;
import org.springframework.data.redis.core.script.RedisScript;

import java.time.Duration;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RedisServiceTest {
    private static final String REFRESH_TOKEN_PREFIX="refresh_token:";

    @Mock
    private HashService hashService;
    @Mock(answer =  Answers.RETURNS_DEEP_STUBS)
    private RedisTemplate<String, byte[]> redisTemplate;
    @Mock
    private RedisScript<Long> redisScript;

    private JwtProperties jwtProperties;

    @InjectMocks
    private RedisService redisService;

    @BeforeEach
    void setUp(){
        jwtProperties = new JwtProperties("test", Duration.ofMinutes(15), Duration.ofDays(30), "keyPr", "keyPb");
        redisService = new RedisService(redisTemplate,redisScript, jwtProperties, hashService);
    }

    @Test
    public void success_shouldSaveHashedRefreshToken(){
        UUID userId = UUID.randomUUID();
        String key = REFRESH_TOKEN_PREFIX + userId;
        byte[] hashedToken = {1, 2, 3};
        String token = "token";
        when(hashService.hash(anyString())).thenReturn(hashedToken);
        when(redisTemplate.opsForValue().setIfAbsent(key, hashedToken, jwtProperties.refreshTtl()))
                .thenReturn(true);



        assertDoesNotThrow(() ->  redisService.saveRefreshToken(userId, token));
        verify(redisTemplate.opsForValue()).setIfAbsent(key, hashedToken, jwtProperties.refreshTtl());
    }

    @Test
    public void failed_shouldReturnRefreshTokenAlreadySavedException(){
        UUID userId = UUID.randomUUID();
        String key = REFRESH_TOKEN_PREFIX + userId;
        byte[] hashedToken = {1, 2, 3};
        String token = "token";

        when(hashService.hash(anyString())).thenReturn(hashedToken);
        when(redisTemplate.opsForValue().setIfAbsent(key, hashedToken, jwtProperties.refreshTtl()))
                .thenReturn(false);

        assertThrows(RefreshTokenAlreadySavedException.class,
                () -> redisService.saveRefreshToken(userId, token));
    }
}
