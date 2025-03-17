package com.example.booking.domain.cinema.cinemaHall.cinemaHall.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class CreateCinemaHallDto {
    private String name;
    private String screenType;
    private String soundSystem;
    private UUID cinemaId;
}
