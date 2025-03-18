package com.example.booking.domain.booking.discount.service;

import com.example.booking.common.pagination.PageDto;
import com.example.booking.domain.booking.discount.dto.CreateDiscountDto;
import com.example.booking.domain.booking.discount.dto.DiscountResponseDto;
import com.example.booking.domain.booking.discount.dto.UpdateDiscountDto;
import com.example.booking.domain.booking.discount.entity.DiscountEntity;
import com.example.booking.domain.booking.discount.mapper.DiscountMapper;
import com.example.booking.domain.booking.discount.repository.DiscountRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class DiscountService implements IDiscountService{

    private final DiscountRepository repository;
    private final DiscountMapper mapper;

    public DiscountService(DiscountRepository repository, DiscountMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public DiscountResponseDto getEntityById(UUID id) {
        return mapper.toResponse(repository.findById(id).orElseThrow(()-> new EntityNotFoundException("Discount not found", new Exception("id"))));
    }

    @Override
    public List<DiscountResponseDto> getAllEntity() {
        return repository.findAll().stream().map(mapper::toResponse).toList();
    }

    @Override
    public DiscountResponseDto createEntity(CreateDiscountDto dto) {
        DiscountEntity entity = mapper.toEntity(dto);
        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public DiscountResponseDto updateEntity(UUID id, UpdateDiscountDto dto) {
        DiscountEntity entity = repository.findById(id).orElseThrow(()-> new EntityNotFoundException("Discount not found", new Exception("id")));
        mapper.updateEntityFromDto(dto, entity);
        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public void deleteEntity(UUID id) {
        DiscountEntity entity = repository.findById(id).orElseThrow(()-> new EntityNotFoundException("Discount not found", new Exception("id")));
        repository.delete(entity);
    }

    @Override
    public PageDto<DiscountResponseDto> searchEntity(Specification<DiscountEntity> spec, Pageable pageable) {
        return new PageDto<>(repository.findAll(spec, pageable).map(mapper::toResponse));
    }
}
