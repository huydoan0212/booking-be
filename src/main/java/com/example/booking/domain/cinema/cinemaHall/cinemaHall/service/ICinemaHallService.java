package com.example.booking.domain.cinema.cinemaHall.cinemaHall.service;

import com.example.booking.common.template.service.ICRUDService;
import com.example.booking.domain.cinema.cinemaHall.cinemaHall.dto.CinemaHallResponseDto;
import com.example.booking.domain.cinema.cinemaHall.cinemaHall.dto.CreateCinemaHallDto;
import com.example.booking.domain.cinema.cinemaHall.cinemaHall.dto.CreateCinemaHallsDto;
import com.example.booking.domain.cinema.cinemaHall.cinemaHall.dto.UpdateCinemaHallDto;
import com.example.booking.domain.cinema.cinemaHall.cinemaHall.entity.CinemaHallEntity;

import java.util.List;
import java.util.UUID;

public interface ICinemaHallService extends ICRUDService<CinemaHallEntity, CreateCinemaHallDto, UpdateCinemaHallDto, CinemaHallResponseDto> {

    boolean creates(UUID cinemaID, List<CreateCinemaHallsDto> dtos);

}
