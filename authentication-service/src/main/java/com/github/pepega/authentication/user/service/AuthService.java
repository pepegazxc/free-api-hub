package com.github.pepega.authentication.user.service;

import com.github.pepega.authentication.user.dto.RegistrationRequest;
import com.github.pepega.authentication.user.entity.UserEntity;
import com.github.pepega.authentication.user.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jasypt.encryption.StringEncryptor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Base64;
import java.util.Locale;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthService {

    private final UsersRepository usersRepository;
    private final StringEncryptor encryptor;
    private final PasswordEncoder passwordEncoder;
    private final HashService hashService;

    @Transactional
    public void register(RegistrationRequest request){
        String normalizedEmail = normalizeString(request.email());

        byte[] cipherEmail = Base64.getDecoder().decode(encryptor.encrypt(normalizedEmail));
        byte[] hashEmail = hashService.hash(normalizedEmail);

        UserEntity user = UserEntity.register(
                normalizeString(request.username()),
                hashEmail,
                cipherEmail,
                passwordEncoder.encode(request.password())
        );

        usersRepository.saveAndFlush(user);

        log.info("User registered, userId={}", user.getId());
    }

    private String normalizeString(String value){
        return value.trim().toLowerCase(Locale.ROOT);
    }
}
