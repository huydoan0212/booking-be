package com.example.nikebe.domain.user.user.dto;

import com.example.nikebe.common.UserStatus;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserDto extends UpdateProfileDTO {

    @Hidden
    private UserStatus userStatus;

    public UpdateUserDto(@NotBlank(message = "Username is required") @NotBlank(message = "Username is required") @Pattern(
            regexp = "^(([\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4})|(\\+84|0)(3|5|7|8|9)\\d{8})$",
            message = "Username must be a valid email or phone number"
    ) String username, @NotBlank(message = "Name is required") String name, Date dob, @Pattern(regexp = "\\d{12}$", message = "Identification number is not valid") String idNumber, int gender, UserStatus userStatus) {
        super(username, name, dob, idNumber, gender);
        this.userStatus = userStatus;
    }
}
