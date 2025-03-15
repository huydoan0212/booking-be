package com.example.booking.domain.movie.category.repository;

import com.example.booking.common.template.repository.CRUDRepository;
import com.example.booking.domain.movie.category.entity.CategoryEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends CRUDRepository<CategoryEntity> {
}
