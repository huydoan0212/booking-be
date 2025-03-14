package com.example.booking.domain.user.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserLoginDto {
    @Schema(
            description = "User email or phone",
            example = "example@example.com or 0123456789"
    )
    private String username;

    @NotBlank(message = "Password is required")
    @Schema(description = "Password", example = "Aa1@test")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$", message = "Password is not valid")
    private String password;
}
