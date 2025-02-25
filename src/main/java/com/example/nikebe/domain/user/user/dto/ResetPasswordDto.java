package com.example.nikebe.domain.user.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ResetPasswordDto {
    private String token;
    private String password;
    private String confirmPassword;
}
