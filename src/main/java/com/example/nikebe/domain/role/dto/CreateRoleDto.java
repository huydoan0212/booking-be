package com.example.nikebe.domain.role.dto;

import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor

public class CreateRoleDto {

    @Pattern(regexp = "^ROLE_[A-Z_]+$\n", message = "Invalid role")
    private String role;

    private String description;
}
