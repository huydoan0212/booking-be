package com.example.booking.domain.user.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class RegisterUserDto {

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Username is required")
    @Pattern(
            regexp = "^(?:[\\w.-]+@(?:[\\w-]+\\.)+[\\w-]{2,4}|\\d{10})$",
            message = "must be a well-formed email or 10-digit phone number"
    )
    @Schema(
            description = "User email or phone",
            example = "example@example.com or 0123456789"
    )
    private String username;

    @Pattern(regexp = "\\d{12}$", message = "Identification number is not valid")
    private String idNumber;

    private int gender;

    @Schema(description = "Date of birth",
            type = "string",
            format = "date",
            example = "2024-12-31")
    private Date dob;

    @NotBlank(message = "Password is required")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$", message = "Password is not valid")
    @Schema(description = "Password", example = "Aa1@test")
    private String password;

    @NotBlank(message = "Confirm password is required")
    @Schema(description = "Confirm Password", example = "Aa1@test")
    private String confirmPassword;

}
