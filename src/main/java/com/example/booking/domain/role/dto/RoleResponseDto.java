package com.example.booking.domain.role.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class RoleResponseDto {
    private UUID id;
    private String role;
    private String roleReference;
    private String description;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
}
