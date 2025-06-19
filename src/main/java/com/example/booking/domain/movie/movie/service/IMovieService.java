package com.example.booking.domain.movie.movie.service;

import com.example.booking.common.template.service.ICRUDService;
import com.example.booking.domain.movie.movie.dto.CreateMovieDto;
import com.example.booking.domain.movie.movie.dto.MovieResponseDto;
import com.example.booking.domain.movie.movie.dto.UpdateMovieDto;
import com.example.booking.domain.movie.movie.entity.MovieEntity;

public interface IMovieService extends ICRUDService<MovieEntity, CreateMovieDto, UpdateMovieDto, MovieResponseDto> {
}
