package com.example.booking.common.template.service;

import com.example.booking.common.pagination.PageDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

public interface ICRUDService<E, C, U, R> {

    R getEntityById(UUID id);

    List<R> getAllEntity();

    @Transactional
    R createEntity(C dto);

    @Transactional
    R updateEntity(UUID id, U dto);

    @Transactional
    @Modifying
    void deleteEntity(UUID id);

    PageDto<R> searchEntity(Specification<E> spec, Pageable pageable);

}
