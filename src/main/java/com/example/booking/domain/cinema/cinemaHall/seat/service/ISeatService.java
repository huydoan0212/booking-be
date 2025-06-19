package com.example.booking.domain.cinema.cinemaHall.seat.service;

import com.example.booking.common.template.service.ICRUDService;
import com.example.booking.domain.cinema.cinemaHall.cinemaHall.entity.CinemaHallEntity;
import com.example.booking.domain.cinema.cinemaHall.seat.dto.CreateSeatDto;
import com.example.booking.domain.cinema.cinemaHall.seat.dto.SeatResponseDto;
import com.example.booking.domain.cinema.cinemaHall.seat.dto.UpdateSeatDto;
import com.example.booking.domain.cinema.cinemaHall.seat.entity.SeatEntity;

import java.util.concurrent.CompletableFuture;

public interface ISeatService extends ICRUDService<SeatEntity, CreateSeatDto, UpdateSeatDto, SeatResponseDto> {

    CompletableFuture<Void> generateSeatsAsync(CinemaHallEntity cinemaHall);

}
