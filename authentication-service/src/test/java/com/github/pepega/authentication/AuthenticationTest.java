package com.github.pepega.authentication;

import com.github.pepega.authentication.user.dto.RegistrationRequest;
import com.github.pepega.authentication.user.entity.UsersEntity;
import com.github.pepega.authentication.user.repository.UsersRepository;
import com.github.pepega.authentication.user.service.AuthService;
import com.github.pepega.authentication.user.service.HashService;
import org.jasypt.encryption.StringEncryptor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

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
    void shouldSuccessfullyRegisterNewUser(){
        RegistrationRequest request = new RegistrationRequest();
        request.setEmail("test");
        request.setUsername("test");
        request.setPassword("test");

        when(stringEncryptor.encrypt(anyString())).thenReturn("cipher");
        when(passwordEncoder.encode(anyString())).thenReturn("hash");
        when(hashService.hash(anyString())).thenReturn("hash");

        authService.addNewUser(request);

        ArgumentCaptor<UsersEntity> captor = ArgumentCaptor.forClass(UsersEntity.class);
        verify(usersRepository).save(captor.capture());
        UsersEntity saved = captor.getValue();

        assertEquals("test", saved.getUsername());
        assertEquals("hash", saved.getHashEmail());
        assertEquals("hash", saved.getHashPassword());
        assertEquals("cipher", saved.getCipherEmail());
    }



}
