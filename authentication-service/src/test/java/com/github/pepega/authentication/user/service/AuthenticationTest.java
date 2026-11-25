package com.github.pepega.authentication.user.service;

import com.github.pepega.authentication.exception.exceptions.EmailAlreadyExistException;
import com.github.pepega.authentication.exception.exceptions.UsernameAlreadyExistException;
import com.github.pepega.authentication.user.dto.AuthTokens;
import com.github.pepega.authentication.user.dto.RegistrationRequest;
import com.github.pepega.authentication.security.service.HashService;
import com.github.pepega.authentication.user.entity.UserEntity;
import com.github.pepega.authentication.user.entity.UserIdentities;
import com.github.pepega.authentication.user.model.ProviderName;
import com.github.pepega.authentication.user.repository.UsersRepository;
import com.github.pepega.authentication.user.repository.UserIdentitiesRepository;
import org.jasypt.encryption.StringEncryptor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthenticationTest {

    @Mock HashService hashService;
    @Mock PasswordEncoder passwordEncoder;
    @Mock StringEncryptor stringEncryptor;
    @Mock UsersRepository usersRepository;
    @Mock UserIdentitiesRepository userIdentitiesRepository;
    @Mock EmailDomainValidatorService domainValidatorService;
    @Mock JwtService jwtService;

    RegistrationRequest request;

    @InjectMocks
    AuthService authService;

    @BeforeEach
    void setUp(){
        request = new RegistrationRequest(
                "  Test ", "  Test@X.com ", "secret"
        );
    }

    @Test
    void success_shouldSuccessfullyRegisterNewUser() {
        byte[] cipherBytes = "cipher".getBytes(StandardCharsets.UTF_8);
        byte[] emailHash = "email-hash".getBytes(StandardCharsets.UTF_8);

        when(stringEncryptor.encrypt("test@x.com"))
                .thenReturn(Base64.getEncoder().encodeToString(cipherBytes));
        when(hashService.hash("test@x.com")).thenReturn(emailHash);
        when(passwordEncoder.encode("secret")).thenReturn("password-hash");
        when(jwtService.saveTokens(any(UserEntity.class))).thenReturn(new AuthTokens(
                "accessToken", "refreshToken"
        ));
        when(domainValidatorService.isEmailDomainValid(any(String.class))).thenReturn(true);

        authService.register(request);

        ArgumentCaptor<UserEntity> userEntityCaptor = ArgumentCaptor.forClass(UserEntity.class);
        ArgumentCaptor<UserIdentities> userIdentitiesCaptor = ArgumentCaptor.forClass(UserIdentities.class);
        verify(userIdentitiesRepository).saveAndFlush(userIdentitiesCaptor.capture());
        verify(usersRepository).saveAndFlush(userEntityCaptor.capture());
        UserEntity savedUser = userEntityCaptor.getValue();
        UserIdentities savedIdentities = userIdentitiesCaptor.getValue();

        assertEquals(ProviderName.LOCAL, savedIdentities.getProvider());
        assertEquals(savedUser, savedIdentities.getUser());
        assertEquals("test", savedUser.getUsername());
        assertArrayEquals(emailHash, savedUser.getHashEmail());
        assertArrayEquals(cipherBytes, savedUser.getCipherEmail());
        assertEquals("password-hash", savedUser.getHashPassword());
    }

    @Test
    void failed_shouldThrowEmailAlreadyExistException(){
        byte[] cipherBytes = "cipher".getBytes(StandardCharsets.UTF_8);
        byte[] emailHash = "email-hash".getBytes(StandardCharsets.UTF_8);

        when(stringEncryptor.encrypt("test@x.com"))
                .thenReturn(Base64.getEncoder().encodeToString(cipherBytes));
        when(hashService.hash("test@x.com")).thenReturn(emailHash);
        when(domainValidatorService.isEmailDomainValid(any(String.class))).thenReturn(true);
        when(usersRepository.existsByHashEmail(emailHash)).thenReturn(true);

        assertThrows(EmailAlreadyExistException.class,
                () -> authService.register(request));
    }

    @Test
    void failed_shouldThrowUsernameAlreadyExistException(){
        byte[] cipherBytes = "cipher".getBytes(StandardCharsets.UTF_8);
        byte[] emailHash = "email-hash".getBytes(StandardCharsets.UTF_8);

        when(stringEncryptor.encrypt("test@x.com"))
                .thenReturn(Base64.getEncoder().encodeToString(cipherBytes));
        when(hashService.hash("test@x.com")).thenReturn(emailHash);
        when(domainValidatorService.isEmailDomainValid(any(String.class))).thenReturn(true);
        when(usersRepository.existsByHashEmail(emailHash)).thenReturn(false);
        when(usersRepository.existsByUsername(request.username().trim().toLowerCase(Locale.ROOT))).thenReturn(true);

        assertThrows(UsernameAlreadyExistException.class,
                () -> authService.register(request));
    }


}
