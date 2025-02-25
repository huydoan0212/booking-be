package com.example.nikebe.domain.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class AuthResponse {

    private String token;

    private long expirationTime;

    private String role;

}
