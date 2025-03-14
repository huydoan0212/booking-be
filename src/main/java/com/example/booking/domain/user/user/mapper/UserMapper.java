package com.example.booking.domain.user.user.mapper;

import com.example.booking.domain.user.user.dto.UserResponseDto;
import com.example.booking.domain.user.user.entity.UserEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserResponseDto userToUserResponseDto(UserEntity user);

}
