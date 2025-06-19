package com.example.booking.domain.role.service;


import com.example.booking.domain.role.dto.CreateRoleDto;
import com.example.booking.domain.role.dto.RoleResponseDto;

import java.util.UUID;

public interface IRoleService {

    RoleResponseDto createRole(CreateRoleDto role);

    RoleResponseDto getRoleById(UUID id);
}
