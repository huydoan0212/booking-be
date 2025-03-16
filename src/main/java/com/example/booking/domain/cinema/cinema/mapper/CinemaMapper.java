package com.example.booking.domain.cinema.cinema.mapper;

import com.example.booking.common.template.mappper.CRUDMapper;
import com.example.booking.domain.cinema.cinema.dto.CinemaResponseDto;
import com.example.booking.domain.cinema.cinema.dto.CreateCinemaDto;
import com.example.booking.domain.cinema.cinema.dto.UpdateCinemaDto;
import com.example.booking.domain.cinema.cinema.entity.CinemaEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CinemaMapper extends CRUDMapper<CinemaEntity, CreateCinemaDto, UpdateCinemaDto, CinemaResponseDto> {
}
