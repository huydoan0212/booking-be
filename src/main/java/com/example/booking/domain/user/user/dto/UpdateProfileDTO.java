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
public class UpdateProfileDTO {

    @NotBlank(message = "Username is required")

    @Schema(
            description = "Username can be either a valid email address or phone number, but not both",
            example = "example@email.com or 0912345678"
    )
    @NotBlank(message = "Username is required")
    @Pattern(
            regexp = "^(([\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4})|(\\+84|0)(3|5|7|8|9)\\d{8})$",
            message = "Username must be a valid email or phone number"
    )
    private String username;
    @NotBlank(message = "Name is required")
    private String name;

    @Schema(description = "Date of birth",
            type = "string",
            format = "date",
            example = "2024-12-31")
    private Date dob;

    @Pattern(regexp = "\\d{12}$", message = "Identification number is not valid")
    private String idNumber;

    private int gender = 0;

}
