package com.example.booking.domain.role.service;


import com.example.booking.domain.role.dto.CreateRoleDto;
import com.example.booking.domain.role.dto.RoleResponseDto;
import com.example.booking.domain.role.entity.RoleEntity;
import com.example.booking.domain.role.mapper.RoleMapper;
import com.example.booking.domain.role.repository.RoleRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class RoleService implements IRoleService {

    private final RoleRepository roleRepository;
    private final ModelMapper modelMapper;
    private final RoleMapper roleMapper;

    public RoleService(RoleRepository roleRepository, ModelMapper modelMapper, RoleMapper roleMapper) {
        this.roleRepository = roleRepository;
        this.modelMapper = modelMapper;
        this.roleMapper = roleMapper;
    }

    public RoleResponseDto createRole(CreateRoleDto role) {
        RoleEntity roleEntity = modelMapper.map(role, RoleEntity.class);
        roleEntity.setRoleReference(role.getRole().substring(5));
        roleEntity = roleRepository.save(roleEntity);
        return roleMapper.roleToRoleResponseDto(roleEntity);
    }

    @Override
    public RoleResponseDto getRoleById(UUID id) {
        return roleMapper.roleToRoleResponseDto(roleRepository.findById(id).orElseThrow(() -> new RuntimeException("Role not found")));
    }


}
