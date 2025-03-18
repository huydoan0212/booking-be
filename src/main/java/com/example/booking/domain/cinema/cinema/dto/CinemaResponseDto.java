package com.example.booking.domain.cinema.cinema.dto;

import com.example.booking.domain.cinema.cinemaHall.cinemaHall.dto.CinemaHallResponseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class CinemaResponseDto {
    private UUID id;
    private String name;
    private String slug;
    private double latitude;
    private double longitude;
    private String address;
    private String phone;
    private String imageLandscape;
    private String imagePortrait;
    private int sortOrder;
    private List<String> imgUrls;
    private List<CinemaHallResponseDto> cinemaHalls;
    private OffsetDateTime createdAt;
}
