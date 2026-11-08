package com.github.pepega.authentication.user.entity;

import com.github.pepega.authentication.user.model.ProfileStatus;
import jakarta.persistence.Entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "users", schema = "user_schema")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserEntity {

    @Id
    @UuidGenerator(style = UuidGenerator.Style.VERSION_7)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;

    @Column(name = "username", nullable = false, unique = true, length = 50)
    private String username;

    @Column(name = "hash_email", nullable = false, unique = true)
    private byte[] hashEmail;

    @Column(name = "cipher_email", nullable = false, unique = true)
    private byte[] cipherEmail;

    @Column(name = "email_verified_at")
    private Instant emailVerifiedAt;

    @Column(name = "hash_password", columnDefinition = "TEXT")
    private String hashPassword;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 25)
    private ProfileStatus status = ProfileStatus.ACTIVE;

    //TO DO: Create methods for entity
}