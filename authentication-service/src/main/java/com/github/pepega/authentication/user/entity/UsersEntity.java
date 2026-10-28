package com.github.pepega.authentication.user.entity;

import jakarta.persistence.Entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class UsersEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", nullable = false, unique = true, length = 50)
    private String username;

    @Column(name = "cipher_email", nullable = false, unique = true)
    private String cipherEmail;

    @Column(name = "hash_password", nullable = false, columnDefinition = "TEXT")
    private String hashPassword;

    @Column(name = "hash_email", nullable = false, unique = true)
    private String hashCipher;

}