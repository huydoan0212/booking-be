package com.example.booking.domain.showTime.mapper;

import com.example.booking.common.template.mappper.CRUDMapper;
import com.example.booking.config.mapper.mapstruct.CentralMapperConfig;
import com.example.booking.domain.showTime.dto.CreateShowTimeDto;
import com.example.booking.domain.showTime.dto.ShowTimeResponseDto;
import com.example.booking.domain.showTime.dto.UpdateShowTimeDto;
import com.example.booking.domain.showTime.entity.ShowTimeEntity;
import org.mapstruct.Mapper;

@Mapper(config = CentralMapperConfig.class)
public interface ShowTimeMapper extends CRUDMapper<ShowTimeEntity, CreateShowTimeDto, UpdateShowTimeDto, ShowTimeResponseDto> {
}
