package com.github.pepega.authentication.user.repository;

import com.github.pepega.authentication.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UsersRepository extends JpaRepository<UserEntity, Long> {
    Boolean existsByUsername(String username);
    Boolean existsByHashEmail(byte[] hashEmail);
    Optional<UserEntity> findById(UUID id);
}
