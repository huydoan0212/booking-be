package com.example.nikebe.domain.user.user.service;


import com.example.nikebe.common.OTPType;
import com.example.nikebe.common.pagination.PageDto;
import com.example.nikebe.domain.user.user.dto.*;
import com.example.nikebe.domain.user.user.entity.UserEntity;
import jakarta.validation.Valid;
import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.UUID;

public interface IUserService {

    @Transactional
    UserResponseDto createByAdmin(@Valid CreateUserDto user) throws IOException, GeneralSecurityException;

    UserResponseDto updateUserByAdmin(UUID id, UpdateUserDto user) throws BadRequestException;

    List<UserResponseDto> getAllUser();

    UserResponseDto getUserById(UUID id);

    UserResponseDto getProfileMe();

    PageDto<UserResponseDto> filterUser(Specification<UserEntity> specification, Pageable pageable);

    UserResponseDto updateProfileUser(UpdateProfileDTO user) throws BadRequestException;

    Boolean deleteUser(UUID id);

    void forgotPassword(String username);

    Object checkOtp(String username, String otp, OTPType otpType) throws BadRequestException;

    Boolean resetPassword(ResetPasswordDto dto) throws BadRequestException;

    Boolean register(RegisterUserDto dto) throws BadRequestException;

    Boolean activeProfile(UserLoginDto dto) throws BadRequestException;
}
