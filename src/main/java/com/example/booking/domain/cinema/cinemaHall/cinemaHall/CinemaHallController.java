package com.example.booking.domain.cinema.cinemaHall.cinemaHall;

import com.example.booking.common.constant.Constant;
import com.example.booking.common.pagination.PageDto;
import com.example.booking.common.pagination.PageOptionsDto;
import com.example.booking.common.template.CRUDController;
import com.example.booking.domain.cinema.cinemaHall.cinemaHall.dto.CinemaHallResponse;
import com.example.booking.domain.cinema.cinemaHall.cinemaHall.dto.CreateCinemaHallDto;
import com.example.booking.domain.cinema.cinemaHall.cinemaHall.dto.CreateCinemaHallsDto;
import com.example.booking.domain.cinema.cinemaHall.cinemaHall.dto.UpdateCinemaHallDto;
import com.example.booking.domain.cinema.cinemaHall.cinemaHall.entity.CinemaHallEntity;
import com.example.booking.domain.cinema.cinemaHall.cinemaHall.service.ICinemaHallService;
import com.turkraft.springfilter.converter.FilterSpecification;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "/cinema-hall", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Cinema Hall")
public class CinemaHallController implements CRUDController<CinemaHallEntity, CreateCinemaHallDto, UpdateCinemaHallDto, CinemaHallResponse> {

    private final ICinemaHallService service;

    public CinemaHallController(ICinemaHallService service) {
        this.service = service;
    }


    @Override
    public CinemaHallResponse getById(UUID id) {
        return service.getEntityById(id);
    }

    @Override
    public List<CinemaHallResponse> getAll() {
        return service.getAllEntity();
    }

    @Override
    public void delete(UUID id) {
        service.deleteEntity(id);
    }

    @Override
    public CinemaHallResponse save(CreateCinemaHallDto dto) {
        return service.createEntity(dto);
    }

    @Override
    public CinemaHallResponse update(UUID id, UpdateCinemaHallDto dto) {
        return service.updateEntity(id, dto);
    }

    @Override
    public PageDto<CinemaHallResponse> search(FilterSpecification<CinemaHallEntity> spec, PageOptionsDto dto) {
        return service.searchEntity(spec, dto.toPageable());
    }

    @SecurityRequirement(name = Constant.AUTH_GUARD)
    @PostMapping("/creates")
    public List<CinemaHallResponse> creates(@RequestParam UUID cinemaID, @RequestBody List<CreateCinemaHallsDto> dtos) {
        return service.creates(cinemaID, dtos);
    }
}
