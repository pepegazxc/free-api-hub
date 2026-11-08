package com.github.pepega.authentication.user.entity;

import com.github.pepega.authentication.user.model.ProviderName;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "user_identities", schema = "user_schema")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserIdentities {

    @Id
    @UuidGenerator(style = UuidGenerator.Style.VERSION_7)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false, updatable = false)
    private UserEntity user;

    @Enumerated(EnumType.STRING)
    @Column(name = "provider_name", nullable = false, updatable = false, length = 25)
    private ProviderName provider;

    @Column(name = "provider_user_id", nullable = false, updatable = false, length = 255)
    private String providerUserId;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;



}
