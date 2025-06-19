package com.example.booking.domain.cinema.cinemaHall.seat.mapper;

import com.example.booking.common.template.mappper.CRUDMapper;
import com.example.booking.config.mapper.mapstruct.CentralMapperConfig;
import com.example.booking.domain.cinema.cinemaHall.seat.dto.CreateSeatDto;
import com.example.booking.domain.cinema.cinemaHall.seat.dto.SeatResponseDto;
import com.example.booking.domain.cinema.cinemaHall.seat.dto.UpdateSeatDto;
import com.example.booking.domain.cinema.cinemaHall.seat.entity.SeatEntity;
import org.mapstruct.Mapper;

@Mapper(config = CentralMapperConfig.class)
public interface SeatMapper extends CRUDMapper<SeatEntity, CreateSeatDto, UpdateSeatDto, SeatResponseDto> {
}
