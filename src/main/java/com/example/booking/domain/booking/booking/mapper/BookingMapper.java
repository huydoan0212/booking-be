package com.example.booking.domain.booking.booking.mapper;

import com.example.booking.common.template.mappper.CRUDMapper;
import com.example.booking.config.mapper.mapstruct.CentralMapperConfig;
import com.example.booking.domain.booking.booking.dto.BookingResponseDto;
import com.example.booking.domain.booking.booking.dto.CreateBookingDto;
import com.example.booking.domain.booking.booking.dto.UpdateBookingDto;
import com.example.booking.domain.booking.booking.entity.BookingEntity;
import org.mapstruct.Mapper;

@Mapper(config = CentralMapperConfig.class)
public interface BookingMapper extends CRUDMapper<BookingEntity, CreateBookingDto, UpdateBookingDto, BookingResponseDto> {
}
