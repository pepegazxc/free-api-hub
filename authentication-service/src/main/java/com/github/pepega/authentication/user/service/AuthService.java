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

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthService {

    private final UsersRepository usersRepository;
    private final StringEncryptor encryptor;
    private final PasswordEncoder passwordEncoder;
    private final HashService hashService;

    @Transactional
    public void addNewUser(RegistrationRequest request){
//        UserEntity user = UserEntity.builder()
//                .username(request.getUsername())
//                .cipherEmail(encryptor.encrypt(request.getEmail()))
//                .hashPassword(passwordEncoder.encode(request.getPassword()))
//                .hashEmail(hashService.hash(request.getEmail()))
//                .build();
//
//        usersRepository.save(user);
//        log.info("User registered, userId={}", user.getId());
    }
}
