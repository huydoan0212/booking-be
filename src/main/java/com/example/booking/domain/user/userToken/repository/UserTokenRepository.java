package com.example.booking.domain.user.userToken.repository;

import com.example.booking.domain.user.userToken.entity.UserTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserTokenRepository extends JpaRepository<UserTokenEntity, UUID> {
    @Query("SELECT ut FROM UserTokenEntity ut WHERE ut.token = :token")
    Optional<UserTokenEntity> findByToken(String token);
}
