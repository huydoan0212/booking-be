package com.example.booking.domain.cinema.cinemaHall.seat.repository;

import com.example.booking.common.template.repository.CRUDRepository;
import com.example.booking.domain.cinema.cinemaHall.seat.entity.SeatEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface SeatRepository extends CRUDRepository<SeatEntity> {
}
