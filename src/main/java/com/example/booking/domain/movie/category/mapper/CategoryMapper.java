package com.example.booking.domain.movie.category.mapper;

import com.example.booking.common.template.mappper.CRUDMapper;
import com.example.booking.config.mapper.mapstruct.CentralMapperConfig;
import com.example.booking.domain.movie.category.dto.CategoryResponseDto;
import com.example.booking.domain.movie.category.dto.CreateCategoryDto;
import com.example.booking.domain.movie.category.dto.UpdateCategoryDto;
import com.example.booking.domain.movie.category.entity.CategoryEntity;
import org.mapstruct.Mapper;

@Mapper(config = CentralMapperConfig.class)
public interface CategoryMapper extends CRUDMapper<CategoryEntity, CreateCategoryDto, UpdateCategoryDto, CategoryResponseDto> {
}
