package com.example.booking.domain.user.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ChangePasswordUserDto {

    @NotBlank(message = "Old password is required")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",message = "Password is not valid")
    @Schema(description = "Password", example = "Aa1@test")
    private String oldPassword;

    @NotBlank(message = "Password is required")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",message = "Password is not valid")
    @Schema(description = "Password", example = "Aa1@test")
    private String password;

    @NotBlank(message = "Confirm password is required")
    @Schema(description = "Confirm Password", example = "Aa1@test")
    private String confirmPassword;

    public ChangePasswordUserDto(String password, String confirmPassword) {
        this.password = password;
        this.confirmPassword = confirmPassword;
    }

}
