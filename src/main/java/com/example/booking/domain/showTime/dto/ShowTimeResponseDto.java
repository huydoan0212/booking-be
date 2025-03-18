package com.example.booking.domain.showTime.dto;

import com.example.booking.domain.cinema.cinemaHall.cinemaHall.dto.CinemaHallResponseDto;
import com.example.booking.domain.movie.movie.dto.MovieResponseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class ShowTimeResponseDto {
    private UUID id;
    private OffsetDateTime showTime;
    private String language;
    private String subtitle;
    private String screenFormat;
    private MovieResponseDto movie;
    private CinemaHallResponseDto cinemaHall;
    private OffsetDateTime createdAt;
}
