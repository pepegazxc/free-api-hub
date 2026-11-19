package com.github.pepega.authentication.user.service;

import com.github.pepega.authentication.security.jwt.JwtIssuer;
import com.github.pepega.authentication.security.jwt.JwtVerifier;
import com.github.pepega.authentication.security.model.TokenPayload;
import com.github.pepega.authentication.security.model.TokenType;
import com.github.pepega.authentication.user.dto.AuthTokens;
import com.github.pepega.authentication.user.entity.UserEntity;
import com.github.pepega.authentication.user.model.ProfileStatus;
import com.github.pepega.authentication.user.repository.UsersRepository;
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
    private final JwtVerifier jwtVerifier;
    private final UsersRepository usersRepository;

    public AuthTokens saveTokens(UserEntity user){
        UUID userid = user.getId();
        String accessToken = jwtIssuer.generateAccessToken(userid);
        String refreshToken = jwtIssuer.generateRefreshToken(userid);

        redisService.saveRefreshToken(userid, refreshToken);

        return new AuthTokens(
                accessToken, refreshToken
        );
    }

    public AuthTokens rotateTokens(String refreshToken){
        TokenPayload payload = jwtVerifier.parse(refreshToken, TokenType.REFRESH_TOKEN);
        UUID userId = payload.userId();

        UserEntity user = usersRepository.findById(userId)
                .orElseThrow(IllegalStateException::new);

        if (user.getStatus() == ProfileStatus.BLOCKED) {
            redisService.deleteToken(userId);
            throw new IllegalStateException();
        }

        String newAccessToken = jwtIssuer.generateAccessToken(userId);
        String newRefreshToken = jwtIssuer.generateRefreshToken(userId);

        boolean rotated = redisService.rotateRefreshToken(
                userId, newRefreshToken, refreshToken
        );

        if (!rotated){
            redisService.deleteToken(userId);
            throw new IllegalStateException();
        }

        return new AuthTokens(
                newAccessToken, newRefreshToken
        );
    }
}
