package com.example.booking.domain.cinema.cinemaHall.cinemaHall.mapper;


import com.example.booking.common.template.mappper.CRUDMapper;
import com.example.booking.domain.cinema.cinemaHall.cinemaHall.dto.CinemaHallResponse;
import com.example.booking.domain.cinema.cinemaHall.cinemaHall.dto.CreateCinemaHallDto;
import com.example.booking.domain.cinema.cinemaHall.cinemaHall.dto.CreateCinemaHallsDto;
import com.example.booking.domain.cinema.cinemaHall.cinemaHall.dto.UpdateCinemaHallDto;
import com.example.booking.domain.cinema.cinemaHall.cinemaHall.entity.CinemaHallEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CinemaHallMapper extends CRUDMapper<CinemaHallEntity, CreateCinemaHallDto, UpdateCinemaHallDto, CinemaHallResponse> {

    CinemaHallEntity toEntity(CreateCinemaHallsDto dto);

}
