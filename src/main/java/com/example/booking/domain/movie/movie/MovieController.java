package com.example.booking.domain.movie.movie;

import com.example.booking.common.pagination.PageDto;
import com.example.booking.common.pagination.PageOptionsDto;
import com.example.booking.common.template.CRUDController;
import com.example.booking.domain.movie.category.dto.CreateCategoryDto;
import com.example.booking.domain.movie.category.dto.UpdateCategoryDto;
import com.example.booking.domain.movie.movie.dto.CreateMovieDto;
import com.example.booking.domain.movie.movie.dto.MovieResponseDto;
import com.example.booking.domain.movie.movie.dto.UpdateMovieDto;
import com.example.booking.domain.movie.movie.entity.MovieEntity;
import com.example.booking.domain.movie.movie.service.MovieService;
import com.turkraft.springfilter.converter.FilterSpecification;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "/movie", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Movie")
public class MovieController implements CRUDController<MovieEntity, CreateMovieDto, UpdateMovieDto, MovieResponseDto> {

    private final MovieService service;

    public MovieController(MovieService service) {
        this.service = service;
    }


    @Override
    public MovieResponseDto getById(UUID id) {
        return service.getEntityById(id);
    }

    @Override
    public List<MovieResponseDto> getAll() {
        return service.getAllEntity();
    }

    @Override
    public void delete(UUID id) {
        service.deleteEntity(id);
    }

    @Override
    public MovieResponseDto save(CreateMovieDto dto) {
        return service.createEntity(dto);
    }

    @Override
    public MovieResponseDto update(UUID id, UpdateMovieDto dto) {
        return service.updateEntity(id, dto);
    }

    @Override
    public PageDto<MovieResponseDto> search(FilterSpecification<MovieEntity> spec, PageOptionsDto dto) {
        return service.searchEntity(spec, dto.toPageable());
    }
}
