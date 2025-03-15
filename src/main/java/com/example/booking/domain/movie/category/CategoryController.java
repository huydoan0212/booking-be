package com.example.booking.domain.movie.category;

import com.example.booking.common.pagination.PageDto;
import com.example.booking.common.pagination.PageOptionsDto;
import com.example.booking.common.template.CRUDController;
import com.example.booking.domain.movie.category.dto.CategoryResponseDto;
import com.example.booking.domain.movie.category.dto.CreateCategoryDto;
import com.example.booking.domain.movie.category.dto.UpdateCategoryDto;
import com.example.booking.domain.movie.category.entity.CategoryEntity;
import com.example.booking.domain.movie.category.service.ICategoryService;
import com.turkraft.springfilter.converter.FilterSpecification;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "/category", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Category")
public class CategoryController implements CRUDController<CategoryEntity, CreateCategoryDto, UpdateCategoryDto, CategoryResponseDto> {

    private final ICategoryService service;

    public CategoryController(ICategoryService service) {
        this.service = service;
    }


    @Override
    public CategoryResponseDto getById(UUID id) {
        return service.getEntityById(id);
    }

    @Override
    public List<CategoryResponseDto> getAll() {
        return service.getAllEntity();
    }

    @Override
    public void delete(UUID id) {
        service.deleteEntity(id);
    }

    @Override
    public CategoryResponseDto save(CreateCategoryDto dto) {
        return service.createEntity(dto);
    }

    @Override
    public CategoryResponseDto update(UUID id, UpdateCategoryDto dto) {
        return service.updateEntity(id, dto);
    }

    @Override
    public PageDto<CategoryResponseDto> search(FilterSpecification<CategoryEntity> spec, PageOptionsDto dto) {
        return service.searchEntity(spec, dto.toPageable());
    }
}
