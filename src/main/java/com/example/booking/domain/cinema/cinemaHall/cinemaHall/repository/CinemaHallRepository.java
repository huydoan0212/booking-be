package com.example.booking.domain.cinema.cinemaHall.cinemaHall.repository;

import com.example.booking.common.template.repository.CRUDRepository;
import com.example.booking.domain.cinema.cinemaHall.cinemaHall.entity.CinemaHallEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface CinemaHallRepository extends CRUDRepository<CinemaHallEntity> {
}
