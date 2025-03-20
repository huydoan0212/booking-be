package com.example.booking.domain.booking.ticket;

import com.example.booking.common.pagination.PageDto;
import com.example.booking.common.pagination.PageOptionsDto;
import com.example.booking.common.template.CRUDController;
import com.example.booking.domain.booking.ticket.dto.CreateTicketDto;
import com.example.booking.domain.booking.ticket.dto.TicketResponseDto;
import com.example.booking.domain.booking.ticket.dto.UpdateTicketDto;
import com.example.booking.domain.booking.ticket.entity.TicketEntity;
import com.example.booking.domain.booking.ticket.service.ITicketService;
import com.turkraft.springfilter.converter.FilterSpecification;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "/ticket", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Ticket")
public class TicketController implements CRUDController<TicketEntity, CreateTicketDto, UpdateTicketDto, TicketResponseDto> {

    private final ITicketService service;

    public TicketController(ITicketService service) {
        this.service = service;
    }

    @Override
    public TicketResponseDto getById(UUID id) {
        return service.getEntityById(id);
    }

    @Override
    public List<TicketResponseDto> getAll() {
        return service.getAllEntity();
    }

    @Override
    public void delete(UUID id) {
        service.deleteEntity(id);
    }

    @Override
    public TicketResponseDto save(CreateTicketDto dto) {
        return service.createEntity(dto);
    }

    @Override
    public TicketResponseDto update(UUID id, UpdateTicketDto dto) {
        return service.updateEntity(id, dto);
    }

    @Override
    public PageDto<TicketResponseDto> search(FilterSpecification<TicketEntity> spec, PageOptionsDto dto) {
        return service.searchEntity(spec, dto.toPageable());
    }
}
