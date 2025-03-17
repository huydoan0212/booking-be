package com.example.booking.domain.cinema.cinemaHall.seat.service;

import com.example.booking.common.enums.status.SeatStatus;
import com.example.booking.common.enums.type.SeatType;
import com.example.booking.common.pagination.PageDto;
import com.example.booking.domain.cinema.cinemaHall.cinemaHall.entity.CinemaHallEntity;
import com.example.booking.domain.cinema.cinemaHall.cinemaHall.repository.CinemaHallRepository;
import com.example.booking.domain.cinema.cinemaHall.seat.dto.CreateSeatDto;
import com.example.booking.domain.cinema.cinemaHall.seat.dto.SeatResponseDto;
import com.example.booking.domain.cinema.cinemaHall.seat.dto.UpdateSeatDto;
import com.example.booking.domain.cinema.cinemaHall.seat.entity.SeatEntity;
import com.example.booking.domain.cinema.cinemaHall.seat.mapper.SeatMapper;
import com.example.booking.domain.cinema.cinemaHall.seat.repository.SeatRepository;
import com.example.booking.domain.user.user.UserDetailsImplement;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class SeatService implements ISeatService {

    private final SeatRepository repository;
    private final SeatMapper mapper;
    private final CinemaHallRepository cinemaHallRepository;

    public SeatService(SeatRepository repository, SeatMapper mapper, CinemaHallRepository cinemaHallRepository) {
        this.repository = repository;
        this.mapper = mapper;
        this.cinemaHallRepository = cinemaHallRepository;
    }

    @Override
    public SeatResponseDto getEntityById(UUID id) {
        return mapper.toResponse(repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Seat not found", new Exception("id"))));
    }

    @Override
    public List<SeatResponseDto> getAllEntity() {
        return repository.findAll().stream().map(mapper::toResponse).toList();
    }

    @Override
    @Transactional
    public SeatResponseDto createEntity(CreateSeatDto dto) {
        SeatEntity entity = mapper.toEntity(dto);
        entity.setCinemaHall(cinemaHallRepository.findById(dto.getCinemaHallId()).orElseThrow(() -> new EntityNotFoundException("CinemaHall not found", new Exception("id"))));
        return mapper.toResponse(repository.save(entity));
    }

    @Override
    @Transactional
    public SeatResponseDto updateEntity(UUID id, UpdateSeatDto dto) {
        SeatEntity entity = repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Seat not found", new Exception("id")));
        mapper.updateEntityFromDto(dto, entity);
        entity.setCinemaHall(cinemaHallRepository.findById(dto.getCinemaHallId()).orElseThrow(() -> new EntityNotFoundException("CinemaHall not found", new Exception("id"))));
        return mapper.toResponse(repository.save(entity));
    }

    @Override
    @Transactional
    public void deleteEntity(UUID id) {
        SeatEntity entity = repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Seat not found", new Exception("id")));
        repository.delete(entity);
    }

    @Override
    public PageDto<SeatResponseDto> searchEntity(Specification<SeatEntity> spec, Pageable pageable) {
        return new PageDto<>(repository.findAll(spec, pageable).map(mapper::toResponse));
    }

    @Async
    @Transactional
    public CompletableFuture<Void> generateSeatsAsync(CinemaHallEntity cinemaHall) {
        String rows = "ABCDEFGHIJKL";
        List<SeatEntity> seats = rows.chars()
                .mapToObj(row -> (char) row)
                .flatMap(row -> IntStream.rangeClosed(1, 14)
                        .mapToObj(col -> {
                            SeatType type = SeatType.STANDARD;
                            if (row == 'L') {
                                type = SeatType.COUPLE;
                            } else if ((row == 'E' || row == 'H' || row == 'F' || row == 'G') && col >= 5 && col <= 10) {
                                type = SeatType.VIP;
                            }
                            return new SeatEntity(String.valueOf(row), col, type, SeatStatus.AVAILABLE, cinemaHall, cinemaHall.getCreatedBy(), cinemaHall.getUpdatedBy());
                        })
                ).collect(Collectors.toList());
        repository.saveAll(seats);
        return CompletableFuture.completedFuture(null);
    }


}