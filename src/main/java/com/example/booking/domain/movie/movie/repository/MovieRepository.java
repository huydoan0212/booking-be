package com.example.booking.domain.movie.movie.repository;

import com.example.booking.common.template.repository.CRUDRepository;
import com.example.booking.domain.movie.movie.entity.MovieEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieRepository extends CRUDRepository<MovieEntity> {
}
