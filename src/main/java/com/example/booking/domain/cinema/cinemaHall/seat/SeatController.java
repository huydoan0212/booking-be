package com.example.booking.domain.cinema.cinemaHall.seat;

import com.example.booking.common.pagination.PageDto;
import com.example.booking.common.pagination.PageOptionsDto;
import com.example.booking.common.template.CRUDController;
import com.example.booking.domain.cinema.cinemaHall.seat.dto.CreateSeatDto;
import com.example.booking.domain.cinema.cinemaHall.seat.dto.SeatResponseDto;
import com.example.booking.domain.cinema.cinemaHall.seat.dto.UpdateSeatDto;
import com.example.booking.domain.cinema.cinemaHall.seat.entity.SeatEntity;
import com.example.booking.domain.cinema.cinemaHall.seat.service.ISeatService;
import com.turkraft.springfilter.converter.FilterSpecification;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "/seat", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Seat")
public class SeatController implements CRUDController<SeatEntity, CreateSeatDto, UpdateSeatDto, SeatResponseDto> {

    private final ISeatService service;

    public SeatController(ISeatService service) {
        this.service = service;
    }

    @Override
    public SeatResponseDto getById(UUID id) {
        return service.getEntityById(id);
    }

    @Override
    public List<SeatResponseDto> getAll() {
        return service.getAllEntity();
    }

    @Override
    public void delete(UUID id) {
        service.deleteEntity(id);
    }

    @Override
    public SeatResponseDto save(CreateSeatDto dto) {
        return service.createEntity(dto);
    }

    @Override
    public SeatResponseDto update(UUID id, UpdateSeatDto dto) {
        return service.updateEntity(id, dto);
    }

    @Override
    public PageDto<SeatResponseDto> search(FilterSpecification<SeatEntity> spec, PageOptionsDto dto) {
        return service.searchEntity(spec, dto.toPageable());
    }
}
