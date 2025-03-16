package com.example.booking.domain.cinema.cinema.service;

import com.example.booking.common.pagination.PageDto;
import com.example.booking.domain.cinema.cinema.dto.CinemaResponseDto;
import com.example.booking.domain.cinema.cinema.dto.CreateCinemaDto;
import com.example.booking.domain.cinema.cinema.dto.UpdateCinemaDto;
import com.example.booking.domain.cinema.cinema.entity.CinemaEntity;
import com.example.booking.domain.cinema.cinema.mapper.CinemaMapper;
import com.example.booking.domain.cinema.cinema.repository.CinemaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CinemaService implements ICinemaService {

    private final CinemaRepository repository;
    private final CinemaMapper mapper;

    public CinemaService(CinemaRepository repository, CinemaMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public CinemaResponseDto getEntityById(UUID id) {
        return mapper.toResponse(repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Cinema not found", new Exception("id"))));
    }

    @Override
    public List<CinemaResponseDto> getAllEntity() {
        return repository.findAll().stream().map(mapper::toResponse).toList();
    }

    @Override
    public CinemaResponseDto createEntity(CreateCinemaDto dto) {
        CinemaEntity entity = mapper.toEntity(dto);
        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public CinemaResponseDto updateEntity(UUID id, UpdateCinemaDto dto) {
        CinemaEntity entity = repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Cinema not found", new Exception("id")));
        mapper.updateEntityFromDto(dto, entity);
        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public void deleteEntity(UUID id) {
        CinemaEntity entity = repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Cinema not found", new Exception("id")));
        repository.delete(entity);
    }

    @Override
    public PageDto<CinemaResponseDto> searchEntity(Specification<CinemaEntity> spec, Pageable pageable) {
        return new PageDto<>(repository.findAll(spec, pageable).map(mapper::toResponse));
    }
}
