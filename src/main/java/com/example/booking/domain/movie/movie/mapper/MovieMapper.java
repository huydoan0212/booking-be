package com.example.booking.domain.movie.movie.mapper;

import com.example.booking.common.template.mappper.CRUDMapper;
import com.example.booking.domain.movie.movie.dto.CreateMovieDto;
import com.example.booking.domain.movie.movie.dto.MovieResponseDto;
import com.example.booking.domain.movie.movie.dto.UpdateMovieDto;
import com.example.booking.domain.movie.movie.entity.MovieEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MovieMapper extends CRUDMapper<MovieEntity, CreateMovieDto, UpdateMovieDto, MovieResponseDto> {
}
