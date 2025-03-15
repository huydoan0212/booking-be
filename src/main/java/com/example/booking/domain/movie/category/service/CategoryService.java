package com.example.booking.domain.movie.category.service;

import com.example.booking.common.pagination.PageDto;
import com.example.booking.domain.movie.category.dto.CategoryResponseDto;
import com.example.booking.domain.movie.category.dto.CreateCategoryDto;
import com.example.booking.domain.movie.category.dto.UpdateCategoryDto;
import com.example.booking.domain.movie.category.entity.CategoryEntity;
import com.example.booking.domain.movie.category.mapper.CategoryMapper;
import com.example.booking.domain.movie.category.repository.CategoryRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CategoryService implements ICategoryService {

    private final CategoryRepository repository;
    private final CategoryMapper mapper;

    public CategoryService(CategoryRepository repository, CategoryMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public CategoryResponseDto getEntityById(UUID id) {
        return mapper.toResponse(repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Category not found", new Exception("id"))));
    }

    @Override
    public List<CategoryResponseDto> getAllEntity() {
        return repository.findAll().stream().map(mapper::toResponse).toList();
    }

    @Override
    public CategoryResponseDto createEntity(CreateCategoryDto dto) {
        CategoryEntity entity = mapper.toEntity(dto);
        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public CategoryResponseDto updateEntity(UUID id, UpdateCategoryDto dto) {
        CategoryEntity entity = repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Category not found", new Exception("id")));
        mapper.updateEntityFromDto(dto, entity);
        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public void deleteEntity(UUID id) {
        CategoryEntity entity = repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Category not found", new Exception("id")));
        repository.delete(entity);
    }

    @Override
    public PageDto<CategoryResponseDto> searchEntity(Specification<CategoryEntity> spec, Pageable pageable) {
        return new PageDto<>(repository.findAll(spec, pageable).map(mapper::toResponse));
    }
}
