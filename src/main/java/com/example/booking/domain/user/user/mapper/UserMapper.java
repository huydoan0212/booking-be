package com.example.booking.domain.user.user.mapper;

import com.example.booking.config.mapper.mapstruct.CentralMapperConfig;
import com.example.booking.domain.user.user.dto.UserResponseDto;
import com.example.booking.domain.user.user.entity.UserEntity;
import org.mapstruct.Mapper;

@Mapper(config = CentralMapperConfig.class)
public interface UserMapper {

    UserResponseDto userToUserResponseDto(UserEntity user);

}
