package com.example.booking.domain.showTime;

import com.example.booking.common.constant.Constant;
import com.example.booking.common.pagination.PageDto;
import com.example.booking.common.pagination.PageOptionsDto;
import com.example.booking.common.template.CRUDController;
import com.example.booking.domain.showTime.dto.CreateShowTimeDto;
import com.example.booking.domain.showTime.dto.ShowTimeResponseDto;
import com.example.booking.domain.showTime.dto.UpdateShowTimeDto;
import com.example.booking.domain.showTime.entity.ShowTimeEntity;
import com.example.booking.domain.showTime.service.IShowTimeService;
import com.turkraft.springfilter.converter.FilterSpecification;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "/show-time", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Show Time")
public class ShowTimeController implements CRUDController<ShowTimeEntity, CreateShowTimeDto, UpdateShowTimeDto, ShowTimeResponseDto> {

    private final IShowTimeService service;

    public ShowTimeController(IShowTimeService service) {
        this.service = service;
    }

    @Override
    public ShowTimeResponseDto getById(UUID id) {
        return service.getEntityById(id);
    }

    @Override
    public List<ShowTimeResponseDto> getAll() {
        return service.getAllEntity();
    }

    @Override
    public void delete(UUID id) {
        service.deleteEntity(id);
    }

    @Override
    public ShowTimeResponseDto save(CreateShowTimeDto dto) {
        return service.createEntity(dto);
    }

    @Override
    public ShowTimeResponseDto update(UUID id, UpdateShowTimeDto dto) {
        return service.updateEntity(id, dto);
    }

    @Override
    public PageDto<ShowTimeResponseDto> search(FilterSpecification<ShowTimeEntity> spec, PageOptionsDto dto) {
        return service.searchEntity(spec, dto.toPageable());
    }

    @SecurityRequirement(name = Constant.AUTH_GUARD)
    @PostMapping("/creates")
    public boolean createShowTimes(@RequestParam UUID movieId,@RequestBody List<CreateShowTimeDto> createShowTimeDtos) {
        return service.createShowTimes(movieId, createShowTimeDtos);
    }
}
