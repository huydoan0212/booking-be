package com.example.booking.domain.cinema.cinema.repository;

import com.example.booking.common.template.repository.CRUDRepository;
import com.example.booking.domain.cinema.cinema.entity.CinemaEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface CinemaRepository extends CRUDRepository<CinemaEntity> {
}
