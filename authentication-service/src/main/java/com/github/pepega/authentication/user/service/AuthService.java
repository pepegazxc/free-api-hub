package com.github.pepega.authentication.user.service;

import com.github.pepega.authentication.user.dto.RegistrationRequest;
import com.github.pepega.authentication.user.entity.UsersEntity;
import com.github.pepega.authentication.user.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthService {

    private final UsersRepository usersRepository;

    @Transactional
    public void addNewUser(RegistrationRequest request){
        UsersEntity user = UsersEntity.builder()
                .username(request.getUsername())
                .cipherEmail(request.getEmail())
                .hashPassword(request.getPassword())
                .build();

        usersRepository.save(user);
        log.info("User registered, userId={}", user.getId());
    }
}
