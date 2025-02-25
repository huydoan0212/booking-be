package com.example.nikebe.domain.user.userOtp.entity;

import com.example.nikebe.common.OTPType;
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
@Table(name = "user_otps")
@EntityListeners(AuditingEntityListener.class)
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class UserOtpEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "otp_code")
    private String otpCode;

    @Column(name = "otp_expired_time")
    private OffsetDateTime otpExpiredTime;

    @Column(name = "used")
    private Boolean used;

    @Enumerated(EnumType.STRING)
    @Column(name = "otp_type")
    private OTPType otpType;

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

    public UserOtpEntity(String otpCode, OffsetDateTime otpExpiredTime, Boolean used, OTPType otpType, UserEntity user) {
        this.otpCode = otpCode;
        this.otpExpiredTime = otpExpiredTime;
        this.used = used;
        this.otpType = otpType;
        this.user = user;
    }
}
