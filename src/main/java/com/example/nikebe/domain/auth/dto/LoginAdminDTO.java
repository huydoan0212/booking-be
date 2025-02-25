package com.example.nikebe.domain.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class LoginAdminDTO {

    @NotNull(message = "email is required")
    @Pattern(
            regexp = "^(([\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4})|(\\+84|0)(3|5|7|8|9)\\d{8})$",
            message = "Username must be a valid email or phone number"
    )
    @Schema(description = "Username", example = "example@example.com")
    private String username;

    @NotNull(message = "password is required")
    @Schema(description = "Password", example = "Aa1@test")
    private String password;

}
