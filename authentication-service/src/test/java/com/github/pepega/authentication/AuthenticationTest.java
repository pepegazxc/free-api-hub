package com.github.pepega.authentication;

import com.github.pepega.authentication.user.dto.RegistrationRequest;
import com.github.pepega.authentication.user.entity.UserEntity;
import com.github.pepega.authentication.user.repository.UsersRepository;
import com.github.pepega.authentication.user.service.AuthService;
import com.github.pepega.authentication.user.service.HashService;
import org.jasypt.encryption.StringEncryptor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthenticationTest {

    @Mock HashService hashService;
    @Mock PasswordEncoder passwordEncoder;
    @Mock StringEncryptor stringEncryptor;
    @Mock UsersRepository usersRepository;

    @InjectMocks
    AuthService authService;

    @Test
    void shouldSuccessfullyRegisterNewUser() {
        RegistrationRequest request = new RegistrationRequest(
                "  Test ", "  TEst@X.com ", "secret"
        );

        byte[] cipherBytes = "cipher".getBytes(StandardCharsets.UTF_8);
        byte[] emailHash = "email-hash".getBytes(StandardCharsets.UTF_8);

        when(stringEncryptor.encrypt("test@x.com"))
                .thenReturn(Base64.getEncoder().encodeToString(cipherBytes));
        when(hashService.hash("test@x.com")).thenReturn(emailHash);
        when(passwordEncoder.encode("secret")).thenReturn("password-hash");

        authService.register(request);

        ArgumentCaptor<UserEntity> captor = ArgumentCaptor.forClass(UserEntity.class);
        verify(usersRepository).saveAndFlush(captor.capture());
        UserEntity saved = captor.getValue();

        assertEquals("test", saved.getUsername());
        assertArrayEquals(emailHash, saved.getHashEmail());
        assertArrayEquals(cipherBytes, saved.getCipherEmail());
        assertEquals("password-hash", saved.getHashPassword());
    }


}
