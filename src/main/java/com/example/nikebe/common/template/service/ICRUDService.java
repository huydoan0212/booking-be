package com.example.nikebe.common.template.service;

import com.example.nikebe.common.pagination.PageDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.UUID;

public interface ICRUDService<E, C, U, R> {

    R getEntityById(UUID id);

    List<R> getAllEntity();

    R createEntity(C dto);

    R updateEntity(UUID id, U dto);

    void deleteEntity(UUID id);

    PageDto<R> searchEntity(Specification<E> spec, Pageable pageable);

}
