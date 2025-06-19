package com.example.booking.domain.user.user;

import com.example.booking.common.enums.type.OTPType;
import com.example.booking.common.enums.Role;
import com.example.booking.common.enums.status.UserStatus;
import com.example.booking.common.constant.Constant;
import com.example.booking.common.pagination.PageDto;
import com.example.booking.common.pagination.PageOptionsDto;
import com.example.booking.domain.user.user.dto.*;
import com.example.booking.domain.user.user.entity.UserEntity;
import com.example.booking.domain.user.user.service.IUserService;
import com.turkraft.springfilter.converter.FilterSpecification;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.apache.coyote.BadRequestException;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;


import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "/user", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "User")
public class UserController {

    private final IUserService userService;

    public UserController(IUserService userService) {
        this.userService = userService;
    }

    @PostMapping(path = "/{role}")
    @SecurityRequirement(name = Constant.AUTH_GUARD)
    public Object createUser(@PathVariable("role") Role role, @RequestBody @Valid CreateUserDto dto) throws IOException, GeneralSecurityException {
        dto.setRole(role);
        return userService.createByAdmin(dto);
    }

    @PutMapping(path = "/{id}")
    @SecurityRequirement(name = Constant.AUTH_GUARD)
    public UserResponseDto updateUser(@PathVariable UUID id, @RequestBody @Valid UpdateUserDto dto, @RequestParam UserStatus userStatus) throws BadRequestException {
        dto.setUserStatus(userStatus);
        return userService.updateUserByAdmin(id, dto);
    }

    @PutMapping(path = "/profile")
    @SecurityRequirement(name = Constant.AUTH_GUARD)
    public UserResponseDto updateProfileUser(@RequestBody @Valid UpdateProfileDTO dto) throws BadRequestException {
        return userService.updateProfileUser(dto);
    }

    @GetMapping(path = "/{id}")
    @SecurityRequirement(name = Constant.AUTH_GUARD)
    @ResponseBody
    public UserResponseDto getUserByID(@PathVariable UUID id) {
        return userService.getUserById(id);
    }

    @GetMapping(path = "/profile")
    @SecurityRequirement(name = Constant.AUTH_GUARD)
    @ResponseBody
    public UserResponseDto profileMe() {
        return userService.getProfileMe();
    }

    @GetMapping(path = "/")
    @SecurityRequirement(name = Constant.AUTH_GUARD)
    @ResponseBody
    public List<UserResponseDto> getAllUser() {
        return userService.getAllUser();
    }

    @Operation(parameters = @Parameter(name = "filter", in = ParameterIn.QUERY, schema = @Schema(type = "string"),
            example = "userStatus.code = 'ACTIVE'"))
    @RequestMapping(path = "/search", method = RequestMethod.GET)
    @SecurityRequirement(name = Constant.AUTH_GUARD)
    PageDto<UserResponseDto> search(@Parameter(hidden = true) FilterSpecification<UserEntity> filter,
                                    @ParameterObject PageOptionsDto pageOptionsDto) {
        Pageable pageable = pageOptionsDto.toPageable();
        return userService.filterUser(filter, pageable);
    }

    @DeleteMapping(path = "/{id}")
    @SecurityRequirement(name = Constant.AUTH_GUARD)
    public void deleteUser(@PathVariable UUID id) {
        userService.deleteUser(id);
    }

    @PostMapping(path = "/forgot-password")
    public void forgotPassword(@RequestParam String username) {
        userService.forgotPassword(username);
    }

    @PostMapping(path = "/check-otp")
    public Object checkOTP(@RequestParam String username, @RequestParam String otp, @RequestParam OTPType otpType) throws BadRequestException {
        return userService.checkOtp(username, otp, otpType);
    }

    @PostMapping(path = "/reset-password")
    public Boolean resetPassword(@RequestBody ResetPasswordDto dto) throws BadRequestException {
        return userService.resetPassword(dto);
    }

    @PostMapping(path = "/register")
    public Boolean register(@RequestBody RegisterUserDto dto) throws BadRequestException {
        return userService.register(dto);
    }

    @PostMapping(path = "/active-profile")
    public Boolean activeProfile(@RequestBody UserLoginDto dto) throws BadRequestException {
        return userService.activeProfile(dto);
    }

}
