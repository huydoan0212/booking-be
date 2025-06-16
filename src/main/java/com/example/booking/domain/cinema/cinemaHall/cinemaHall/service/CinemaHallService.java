package com.example.booking.domain.cinema.cinemaHall.cinemaHall.service;

import com.example.booking.common.pagination.PageDto;
import com.example.booking.domain.cinema.cinema.entity.CinemaEntity;
import com.example.booking.domain.cinema.cinema.repository.CinemaRepository;
import com.example.booking.domain.cinema.cinemaHall.cinemaHall.dto.CinemaHallResponseDto;
import com.example.booking.domain.cinema.cinemaHall.cinemaHall.dto.CreateCinemaHallDto;
import com.example.booking.domain.cinema.cinemaHall.cinemaHall.dto.CreateCinemaHallsDto;
import com.example.booking.domain.cinema.cinemaHall.cinemaHall.dto.UpdateCinemaHallDto;
import com.example.booking.domain.cinema.cinemaHall.cinemaHall.entity.CinemaHallEntity;
import com.example.booking.domain.cinema.cinemaHall.cinemaHall.mapper.CinemaHallMapper;
import com.example.booking.domain.cinema.cinemaHall.cinemaHall.repository.CinemaHallRepository;
import com.example.booking.domain.cinema.cinemaHall.seat.repository.SeatRepository;
import com.example.booking.domain.cinema.cinemaHall.seat.service.ISeatService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class CinemaHallService implements ICinemaHallService {

    private final CinemaHallRepository repository;
    private final CinemaHallMapper mapper;
    private final CinemaRepository cinemaRepository;
    private final ISeatService seatService;

    public CinemaHallService(CinemaHallRepository repository, CinemaHallMapper mapper, CinemaRepository cinemaRepository, ISeatService seatService, SeatRepository seatRepository) {
        this.repository = repository;
        this.mapper = mapper;
        this.cinemaRepository = cinemaRepository;
        this.seatService = seatService;
    }

    @Override
    public CinemaHallResponseDto getEntityById(UUID id) {
        return mapper.toResponse(repository.findById(id).orElseThrow(() -> new EntityNotFoundException("CinemaHall not found", new Exception("id"))));
    }

    @Override
    public List<CinemaHallResponseDto> getAllEntity() {
        return repository.findAll().stream().map(mapper::toResponse).toList();
    }

    @Override
    @Transactional
    public CinemaHallResponseDto createEntity(CreateCinemaHallDto dto) {
        CinemaEntity cinema = cinemaRepository.findById(dto.getCinemaId())
                .orElseThrow(() -> new EntityNotFoundException("Cinema not found"));
        CinemaHallEntity entity = mapper.toEntity(dto);
        entity.setCinema(cinema);
        CinemaHallEntity savedEntity = repository.save(entity);
        seatService.generateSeatsAsync(savedEntity);
        return mapper.toResponse(savedEntity);
    }

    @Override
    @Transactional
    public CinemaHallResponseDto updateEntity(UUID id, UpdateCinemaHallDto dto) {
        CinemaHallEntity entity = repository.findById(id).orElseThrow(() -> new EntityNotFoundException("CinemaHall not found", new Exception("id")));
        mapper.updateEntityFromDto(dto, entity);
        entity.setCinema(cinemaRepository.findById(dto.getCinemaId()).orElseThrow(() -> new EntityNotFoundException("Cinema not found", new Exception("id"))));
        return mapper.toResponse(repository.save(entity));
    }

    @Override
    @Transactional
    public void deleteEntity(UUID id) {
        CinemaHallEntity entity = repository.findById(id).orElseThrow(() -> new EntityNotFoundException("CinemaHall not found", new Exception("id")));
        repository.delete(entity);
    }

    @Override
    public PageDto<CinemaHallResponseDto> searchEntity(Specification<CinemaHallEntity> spec, Pageable pageable) {
        return new PageDto<>(repository.findAll(spec, pageable).map(mapper::toResponse));
    }

    @Override
    @Transactional
    public boolean creates(UUID cinemaID, List<CreateCinemaHallsDto> dtos) {
        CinemaEntity cinema = cinemaRepository.findById(cinemaID)
                .orElseThrow(() -> new EntityNotFoundException("Cinema not found"));

        List<CinemaHallEntity> cinemaHalls = dtos.stream().map(dto -> {
            CinemaHallEntity entity = mapper.toEntity(dto);
            entity.setCinema(cinema);
            return entity;
        }).toList();
        List<CinemaHallEntity> savedCinemaHalls = repository.saveAll(cinemaHalls);
        savedCinemaHalls.forEach(seatService::generateSeatsAsync);
        return true;
    }


}
