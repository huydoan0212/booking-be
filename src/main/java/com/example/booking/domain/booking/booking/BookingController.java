package com.example.booking.domain.booking.booking;

import com.example.booking.common.pagination.PageDto;
import com.example.booking.common.pagination.PageOptionsDto;
import com.example.booking.common.template.CRUDController;
import com.example.booking.domain.booking.booking.dto.BookingResponseDto;
import com.example.booking.domain.booking.booking.dto.CreateBookingDto;
import com.example.booking.domain.booking.booking.dto.UpdateBookingDto;
import com.example.booking.domain.booking.booking.entity.BookingEntity;
import com.example.booking.domain.booking.booking.service.IBookingService;
import com.turkraft.springfilter.converter.FilterSpecification;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "/booking", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Booking")
public class BookingController implements CRUDController<BookingEntity, CreateBookingDto, UpdateBookingDto, BookingResponseDto> {

    private final IBookingService service;

    public BookingController(IBookingService service) {
        this.service = service;
    }


    @Override
    public BookingResponseDto getById(UUID id) {
        return service.getEntityById(id);
    }

    @Override
    public List<BookingResponseDto> getAll() {
        return service.getAllEntity();
    }

    @Override
    public void delete(UUID id) {
        service.deleteEntity(id);
    }

    @Override
    public BookingResponseDto save(CreateBookingDto dto) {
        return service.createEntity(dto);
    }

    @Override
    public BookingResponseDto update(UUID id, UpdateBookingDto dto) {
        return service.updateEntity(id, dto);
    }

    @Override
    public PageDto<BookingResponseDto> search(FilterSpecification<BookingEntity> spec, PageOptionsDto dto) {
        return service.searchEntity(spec, dto.toPageable());
    }
}
