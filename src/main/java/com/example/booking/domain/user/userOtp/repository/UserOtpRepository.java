package com.example.booking.domain.user.userOtp.repository;

import com.example.booking.common.OTPType;
import com.example.booking.domain.user.userOtp.entity.UserOtpEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserOtpRepository extends JpaRepository<UserOtpEntity, UUID> {

    @Transactional
    @Modifying
    @Query("DELETE FROM UserOtpEntity u WHERE u.user.id = :userId AND u.otpType = :otpType")
    void deleteOtpsByUserIdAndType(@Param("userId") UUID userId, @Param("otpType") OTPType otpType);


    @Query("SELECT u FROM UserOtpEntity u WHERE u.user.id = :userId AND u.otpType = :otpType")
    Optional<UserOtpEntity> findForgotOtpByUserId(@Param("userId") UUID userId, @Param("otpType") OTPType otpType);
}
