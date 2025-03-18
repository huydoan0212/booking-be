package com.example.booking.domain.booking.discount.mapper;

import com.example.booking.common.template.mappper.CRUDMapper;
import com.example.booking.config.mapper.mapstruct.CentralMapperConfig;
import com.example.booking.domain.booking.discount.dto.CreateDiscountDto;
import com.example.booking.domain.booking.discount.dto.DiscountResponseDto;
import com.example.booking.domain.booking.discount.dto.UpdateDiscountDto;
import com.example.booking.domain.booking.discount.entity.DiscountEntity;
import org.mapstruct.Mapper;

@Mapper(config = CentralMapperConfig.class)
public interface DiscountMapper extends CRUDMapper<DiscountEntity, CreateDiscountDto, UpdateDiscountDto, DiscountResponseDto> {
}
