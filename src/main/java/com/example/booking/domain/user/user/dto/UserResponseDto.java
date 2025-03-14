package com.example.booking.domain.user.user.dto;

import com.example.booking.common.UserStatus;
import com.example.booking.domain.role.dto.RoleResponseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.sql.Date;
import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class UserResponseDto {
    private UUID id;
    private String username;
    private String name;
    private Date dob;
    private String idNumber;
    private short gender;
    private OffsetDateTime createdAt;
    private UUID createdBy;
    private OffsetDateTime updatedAt;
    private UUID updatedBy;
    private RoleResponseDto userRole;
    private UserStatus userStatus;

}
