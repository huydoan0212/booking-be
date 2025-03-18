package com.example.booking.domain.cinema.cinemaHall.cinemaHall.mapper;


import com.example.booking.common.template.mappper.CRUDMapper;
import com.example.booking.config.mapper.mapstruct.CentralMapperConfig;
import com.example.booking.domain.cinema.cinemaHall.cinemaHall.dto.CinemaHallResponseDto;
import com.example.booking.domain.cinema.cinemaHall.cinemaHall.dto.CreateCinemaHallDto;
import com.example.booking.domain.cinema.cinemaHall.cinemaHall.dto.CreateCinemaHallsDto;
import com.example.booking.domain.cinema.cinemaHall.cinemaHall.dto.UpdateCinemaHallDto;
import com.example.booking.domain.cinema.cinemaHall.cinemaHall.entity.CinemaHallEntity;
import org.mapstruct.Mapper;

@Mapper(config = CentralMapperConfig.class)
public interface CinemaHallMapper extends CRUDMapper<CinemaHallEntity, CreateCinemaHallDto, UpdateCinemaHallDto, CinemaHallResponseDto> {

    CinemaHallEntity toEntity(CreateCinemaHallsDto dto);

}
