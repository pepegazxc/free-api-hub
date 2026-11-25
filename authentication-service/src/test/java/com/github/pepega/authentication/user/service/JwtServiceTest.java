package com.github.pepega.authentication.user.service;

import com.github.pepega.authentication.exception.exceptions.TokenRotateException;
import com.github.pepega.authentication.exception.exceptions.UserNotFoundException;
import com.github.pepega.authentication.exception.exceptions.UserStatusException;
import com.github.pepega.authentication.security.model.TokenPayload;
import com.github.pepega.authentication.security.model.TokenType;
import com.github.pepega.authentication.user.entity.UserEntity;
import com.github.pepega.authentication.user.model.ProfileStatus;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.github.pepega.authentication.security.jwt.JwtIssuer;
import com.github.pepega.authentication.security.jwt.JwtVerifier;
import com.github.pepega.authentication.user.repository.UsersRepository;
import com.github.pepega.authentication.user.dto.AuthTokens;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class JwtServiceTest {

    @Mock JwtIssuer jwtIssuer;
    @Mock JwtVerifier jwtVerifier;
    @Mock RedisService redisService;
    @Mock UsersRepository usersRepository;

    private JwtService jwtService;

    UUID userId = UUID.randomUUID();

    @BeforeEach
    void setUp(){
        jwtService = new JwtService(jwtIssuer, redisService, jwtVerifier, usersRepository);
    }


    @Nested
    @DisplayName("Success")
    class SuccessTests {

        @Test
        void success_saveTokens_shouldSuccessSaveTokens(){
            UserEntity user = mock(UserEntity.class);
            String refreshToken = "refresh-token";
            String accessToken = "access-token";

            when(jwtIssuer.generateAccessToken(userId)).thenReturn(accessToken);
            when(jwtIssuer.generateRefreshToken(userId)).thenReturn(refreshToken);
            when(user.getId()).thenReturn(userId);

            AuthTokens result = jwtService.saveTokens(user);

            assertNotNull(result);
            assertEquals("refresh-token", result.refreshToken());
            assertEquals("access-token", result.accessToken());
            verify(redisService).saveRefreshToken(userId, refreshToken);
        }

        @Test
        void success_rotateTokens_shouldSuccessRotateTokens(){
            TokenPayload payload = new TokenPayload(
                    userId, TokenType.REFRESH_TOKEN, UUID.randomUUID()
            );
            UserEntity user = mock(UserEntity.class);
            String oldRefreshToken = "old-refresh-token";
            String refreshToken = "refresh-token";
            String accessToken = "access-token";

            when(jwtVerifier.parse(oldRefreshToken, TokenType.REFRESH_TOKEN)).thenReturn(payload);
            when(usersRepository.findById(userId)).thenReturn(Optional.ofNullable(user));
            when(jwtIssuer.generateAccessToken(userId)).thenReturn(accessToken);
            when(jwtIssuer.generateRefreshToken(userId)).thenReturn(refreshToken);
            when(redisService.rotateRefreshToken(userId, refreshToken, oldRefreshToken)).thenReturn(true);

            AuthTokens result = jwtService.rotateTokens(oldRefreshToken);

            assertNotNull(result);
            assertEquals("access-token", result.accessToken());
            assertEquals("refresh-token", result.refreshToken());
        }
    }

    @Nested
    @DisplayName("Failed")
    class FailedTests{

        @Test
        void failed_rotateTokens_shouldReturnUserNotFoundException(){
            TokenPayload payload = new TokenPayload(
                    userId, TokenType.REFRESH_TOKEN, UUID.randomUUID()
            );
            UserEntity user = mock(UserEntity.class);
            String oldRefreshToken = "old-refresh-token";

            when(jwtVerifier.parse(oldRefreshToken, TokenType.REFRESH_TOKEN)).thenReturn(payload);
            when(usersRepository.findById(userId)).thenThrow(UserNotFoundException.class);

            assertThrows(UserNotFoundException.class,
                    () -> jwtService.rotateTokens(oldRefreshToken));
        }

        @Test
        void failed_rotateTokens_shouldReturnUserStatusException(){
            TokenPayload payload = new TokenPayload(
                    userId, TokenType.REFRESH_TOKEN, UUID.randomUUID()
            );
            UserEntity user = mock(UserEntity.class);
            String oldRefreshToken = "old-refresh-token";

            when(user.getStatus()).thenReturn(ProfileStatus.BLOCKED);
            when(jwtVerifier.parse(oldRefreshToken, TokenType.REFRESH_TOKEN)).thenReturn(payload);
            when(usersRepository.findById(userId)).thenReturn(Optional.of(user));

            assertThrows(UserStatusException.class,
                    () -> jwtService.rotateTokens(oldRefreshToken));
        }

        @Test
        void failed_rotateTokens_shouldReturnTokenRotateException(){
            TokenPayload payload = new TokenPayload(
                    userId, TokenType.REFRESH_TOKEN, UUID.randomUUID()
            );
            UserEntity user = mock(UserEntity.class);
            String oldRefreshToken = "old-refresh-token";
            String refreshToken = "refresh-token";

            when(jwtIssuer.generateRefreshToken(userId)).thenReturn(refreshToken);
            when(jwtVerifier.parse(oldRefreshToken, TokenType.REFRESH_TOKEN)).thenReturn(payload);
            when(usersRepository.findById(userId)).thenReturn(Optional.of(user));
            when(redisService.rotateRefreshToken(userId, refreshToken, oldRefreshToken)).thenReturn(false);

            assertThrows(TokenRotateException.class,
                    () -> jwtService.rotateTokens(oldRefreshToken));

            verify(redisService).deleteToken(userId);
        }
    }
}
