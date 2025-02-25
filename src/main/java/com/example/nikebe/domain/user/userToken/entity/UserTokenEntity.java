package com.example.nikebe.domain.user.userToken.entity;

import com.example.nikebe.domain.user.user.entity.UserEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.OffsetDateTime;
import java.util.UUID;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "user_tokens")
@EntityListeners(AuditingEntityListener.class)
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class UserTokenEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "token")
    private String token;

    @Column(name = "expired_at")
    private OffsetDateTime expiredAt;

    @Column(name = "used")
    private Boolean used;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false, nullable = false)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private OffsetDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private OffsetDateTime updatedAt;

    public UserTokenEntity(String token, OffsetDateTime expiredAt, Boolean used, UserEntity user) {
        this.token = token;
        this.expiredAt = expiredAt;
        this.used = used;
        this.user = user;
    }
}
