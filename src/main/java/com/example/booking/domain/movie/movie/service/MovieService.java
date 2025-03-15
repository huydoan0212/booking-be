package com.example.booking.domain.movie.movie.service;

import com.example.booking.common.pagination.PageDto;
import com.example.booking.domain.movie.category.entity.CategoryEntity;
import com.example.booking.domain.movie.category.repository.CategoryRepository;
import com.example.booking.domain.movie.movie.dto.CreateMovieDto;
import com.example.booking.domain.movie.movie.dto.MovieResponseDto;
import com.example.booking.domain.movie.movie.dto.UpdateMovieDto;
import com.example.booking.domain.movie.movie.entity.MovieEntity;
import com.example.booking.domain.movie.movie.mapper.MovieMapper;
import com.example.booking.domain.movie.movie.repository.MovieRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class MovieService implements IMovieService {

    private final MovieRepository repository;
    private final MovieMapper mapper;
    private final CategoryRepository categoryRepository;

    public MovieService(MovieRepository repository, MovieMapper mapper, CategoryRepository categoryRepository) {
        this.repository = repository;
        this.mapper = mapper;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public MovieResponseDto getEntityById(UUID id) {
        return mapper.toResponse(repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Movie not found", new Exception("id"))));
    }

    @Override
    public List<MovieResponseDto> getAllEntity() {
        return repository.findAll().stream().map(mapper::toResponse).toList();
    }

    @Override
    public MovieResponseDto createEntity(CreateMovieDto dto) {
        MovieEntity entity = mapper.toEntity(dto);
        List<CategoryEntity> categories = categoryRepository.findAllById(dto.getCategoryIds());
        entity.setCategories(new HashSet<>(categories));
        return mapper.toResponse(repository.save(entity));
    }


    @Override
    public MovieResponseDto updateEntity(UUID id, UpdateMovieDto dto) {
        MovieEntity entity = repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Movie not found", new Exception("id")));
        mapper.updateEntityFromDto(dto, entity);
        List<CategoryEntity> categories = categoryRepository.findAllById(dto.getCategoryIds());
        entity.setCategories(new HashSet<>(categories));
        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public void deleteEntity(UUID id) {
        MovieEntity entity = repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Movie not found", new Exception("id")));
        repository.delete(entity);
    }

    @Override
    public PageDto<MovieResponseDto> searchEntity(Specification<MovieEntity> spec, Pageable pageable) {
        return new PageDto<>(repository.findAll(spec, pageable).map(mapper::toResponse));
    }
}
