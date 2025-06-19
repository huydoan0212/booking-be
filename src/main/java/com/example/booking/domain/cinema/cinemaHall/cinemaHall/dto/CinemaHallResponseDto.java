package com.example.booking.domain.cinema.cinemaHall.cinemaHall.dto;

import com.example.booking.domain.cinema.cinema.dto.CinemaResponseDto;
import com.example.booking.domain.cinema.cinemaHall.seat.dto.SeatResponseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class CinemaHallResponseDto {
    private UUID id;
    private String name;
    private int totalSeats;
    private String screenType;
    private String soundSystem;
    private CinemaResponseDto cinema;
    private OffsetDateTime createdAt;
//    private List<SeatResponseDto> seats;
}
