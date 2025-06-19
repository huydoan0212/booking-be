package com.example.booking.config.mapper.mapstruct;

import org.mapstruct.MapperConfig;

@MapperConfig(componentModel = "spring", uses = {CommonOffsetDateTimeMapper.class})
public interface CentralMapperConfig {
}
