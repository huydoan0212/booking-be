package com.example.booking.domain.movie.category.service;

import com.example.booking.common.template.service.ICRUDService;
import com.example.booking.domain.movie.category.dto.CategoryResponseDto;
import com.example.booking.domain.movie.category.dto.CreateCategoryDto;
import com.example.booking.domain.movie.category.dto.UpdateCategoryDto;
import com.example.booking.domain.movie.category.entity.CategoryEntity;

public interface ICategoryService extends ICRUDService<CategoryEntity, CreateCategoryDto, UpdateCategoryDto, CategoryResponseDto> {
}
