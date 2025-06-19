package com.example.booking.domain.cinema.cinema.service;

import com.example.booking.common.template.service.ICRUDService;
import com.example.booking.domain.cinema.cinema.dto.CinemaResponseDto;
import com.example.booking.domain.cinema.cinema.dto.CreateCinemaDto;
import com.example.booking.domain.cinema.cinema.dto.UpdateCinemaDto;
import com.example.booking.domain.cinema.cinema.entity.CinemaEntity;

public interface ICinemaService extends ICRUDService<CinemaEntity, CreateCinemaDto, UpdateCinemaDto, CinemaResponseDto> {
}
