package com.example.booking.domain.cinema.cinema;

import com.example.booking.common.pagination.PageDto;
import com.example.booking.common.pagination.PageOptionsDto;
import com.example.booking.common.template.CRUDController;
import com.example.booking.domain.cinema.cinema.dto.CinemaResponseDto;
import com.example.booking.domain.cinema.cinema.dto.CreateCinemaDto;
import com.example.booking.domain.cinema.cinema.dto.UpdateCinemaDto;
import com.example.booking.domain.cinema.cinema.entity.CinemaEntity;
import com.example.booking.domain.cinema.cinema.service.ICinemaService;
import com.turkraft.springfilter.converter.FilterSpecification;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "/cinema", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Cinema")
public class CinemaController implements CRUDController<CinemaEntity, CreateCinemaDto, UpdateCinemaDto, CinemaResponseDto> {

    private final ICinemaService service;

    public CinemaController(ICinemaService service) {
        this.service = service;
    }

    @Override
    public CinemaResponseDto getById(UUID id) {
        return service.getEntityById(id);
    }

    @Override
    public List<CinemaResponseDto> getAll() {
        return service.getAllEntity();
    }

    @Override
    public void delete(UUID id) {
        service.deleteEntity(id);
    }

    @Override
    public CinemaResponseDto save(CreateCinemaDto dto) {
        return service.createEntity(dto);
    }

    @Override
    public CinemaResponseDto update(UUID id, UpdateCinemaDto dto) {
        return service.updateEntity(id, dto);
    }

    @Override
    public PageDto<CinemaResponseDto> search(FilterSpecification<CinemaEntity> spec, PageOptionsDto dto) {
        return service.searchEntity(spec, dto.toPageable());
    }
}
