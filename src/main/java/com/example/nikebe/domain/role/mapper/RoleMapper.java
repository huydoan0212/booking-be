package com.example.nikebe.domain.role.mapper;

import com.example.nikebe.domain.role.dto.RoleResponseDto;
import com.example.nikebe.domain.role.entity.RoleEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    RoleResponseDto roleToRoleResponseDto(RoleEntity role);
}
