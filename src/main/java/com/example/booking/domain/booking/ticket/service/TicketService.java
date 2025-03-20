package com.example.booking.domain.booking.ticket.service;

import com.example.booking.common.pagination.PageDto;
import com.example.booking.domain.booking.booking.repository.BookingRepository;
import com.example.booking.domain.booking.ticket.dto.CreateTicketDto;
import com.example.booking.domain.booking.ticket.dto.TicketResponseDto;
import com.example.booking.domain.booking.ticket.dto.UpdateTicketDto;
import com.example.booking.domain.booking.ticket.entity.TicketEntity;
import com.example.booking.domain.booking.ticket.mapper.TicketMapper;
import com.example.booking.domain.booking.ticket.repository.TicketRepository;
import com.example.booking.domain.cinema.cinemaHall.seat.repository.SeatRepository;
import com.example.booking.domain.showTime.repository.ShowTimeRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class TicketService implements ITicketService{

    private final TicketRepository repository;
    private final TicketMapper mapper;

    private final ShowTimeRepository showTimeRepository;

    private final SeatRepository seatRepository;

    private final BookingRepository bookingRepository;

    public TicketService(TicketRepository repository, TicketMapper mapper, ShowTimeRepository showTimeRepository, SeatRepository seatRepository, BookingRepository bookingRepository) {
        this.repository = repository;
        this.mapper = mapper;
        this.showTimeRepository = showTimeRepository;
        this.seatRepository = seatRepository;
        this.bookingRepository = bookingRepository;
    }

    @Override
    public TicketResponseDto getEntityById(UUID id) {
        return mapper.toResponse(repository.findById(id).orElseThrow(()-> new EntityNotFoundException("Ticket not found", new Exception("id"))));
    }

    @Override
    public List<TicketResponseDto> getAllEntity() {
        return repository.findAll().stream().map(mapper::toResponse).toList();
    }

    @Override
    public TicketResponseDto createEntity(CreateTicketDto dto) {
        TicketEntity entity = mapper.toEntity(dto);
        entity.setShowTime(showTimeRepository.findById(dto.getShowTimeId()).orElseThrow(()-> new EntityNotFoundException("Show Time not found", new Exception("id"))));
        entity.setSeat(seatRepository.findById(dto.getSeatId()).orElseThrow(()-> new EntityNotFoundException("Seat not found", new Exception("id"))));
        entity.setBooking(bookingRepository.findById(dto.getBookingId()).orElseThrow(()-> new EntityNotFoundException("Booking not found", new Exception("id"))));
        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public TicketResponseDto updateEntity(UUID id, UpdateTicketDto dto) {
        TicketEntity entity = repository.findById(id).orElseThrow(()-> new EntityNotFoundException("Ticket not found", new Exception("id")));
        mapper.updateEntityFromDto(dto, entity);
        entity.setShowTime(showTimeRepository.findById(dto.getShowTimeId()).orElseThrow(()-> new EntityNotFoundException("Show Time not found", new Exception("id"))));
        entity.setSeat(seatRepository.findById(dto.getSeatId()).orElseThrow(()-> new EntityNotFoundException("Seat not found", new Exception("id"))));
        entity.setBooking(bookingRepository.findById(dto.getBookingId()).orElseThrow(()-> new EntityNotFoundException("Booking not found", new Exception("id"))));
        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public void deleteEntity(UUID id) {
        TicketEntity entity = repository.findById(id).orElseThrow(()-> new EntityNotFoundException("Ticket not found", new Exception("id")));
        repository.delete(entity);
    }

    @Override
    public PageDto<TicketResponseDto> searchEntity(Specification<TicketEntity> spec, Pageable pageable) {
        return new PageDto<>(repository.findAll(spec, pageable).map(mapper::toResponse));
    }
}
