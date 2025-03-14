package com.example.booking.domain.role.mapper;

import com.example.booking.domain.role.dto.RoleResponseDto;
import com.example.booking.domain.role.entity.RoleEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    RoleResponseDto roleToRoleResponseDto(RoleEntity role);
}
