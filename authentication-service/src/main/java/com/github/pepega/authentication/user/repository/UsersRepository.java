package com.github.pepega.authentication.user.repository;

import com.github.pepega.authentication.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersRepository extends JpaRepository<UserEntity, Long> {
    Boolean existsByUsername(String username);
    Boolean existsByHashEmail(byte[] hashEmail);
}
