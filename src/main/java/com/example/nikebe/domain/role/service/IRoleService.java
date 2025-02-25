package com.example.nikebe.domain.role.service;


import com.example.nikebe.domain.role.dto.CreateRoleDto;
import com.example.nikebe.domain.role.dto.RoleResponseDto;

import java.util.UUID;

public interface IRoleService {

    RoleResponseDto createRole(CreateRoleDto role);

    RoleResponseDto getRoleById(UUID id);
}
