package com.example.booking.domain.booking.ticket.service;

import com.example.booking.common.enums.status.TicketStatus;
import com.example.booking.common.enums.type.TicketType;
import com.example.booking.common.helper.CodeGenerator;
import com.example.booking.common.pagination.PageDto;
import com.example.booking.domain.booking.booking.repository.BookingRepository;
import com.example.booking.domain.booking.ticket.dto.CreateTicketDto;
import com.example.booking.domain.booking.ticket.dto.TicketResponseDto;
import com.example.booking.domain.booking.ticket.dto.UpdateTicketDto;
import com.example.booking.domain.booking.ticket.entity.TicketEntity;
import com.example.booking.domain.booking.ticket.mapper.TicketMapper;
import com.example.booking.domain.booking.ticket.repository.TicketRepository;
import com.example.booking.domain.cinema.cinemaHall.cinemaHall.entity.CinemaHallEntity;
import com.example.booking.domain.cinema.cinemaHall.cinemaHall.repository.CinemaHallRepository;
import com.example.booking.domain.cinema.cinemaHall.seat.entity.SeatEntity;
import com.example.booking.domain.cinema.cinemaHall.seat.repository.SeatRepository;
import com.example.booking.domain.showTime.entity.ShowTimeEntity;
import com.example.booking.domain.showTime.repository.ShowTimeRepository;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static java.util.Arrays.stream;

@Service
public class TicketService implements ITicketService {

    private static final Logger log = LoggerFactory.getLogger(TicketService.class);
    private final TicketRepository repository;
    private final TicketMapper mapper;
    private final ShowTimeRepository showTimeRepository;
    private final SeatRepository seatRepository;
    private final BookingRepository bookingRepository;
    private final CodeGenerator codeGenerator;
    private final CinemaHallRepository cinemaHallRepository;

    public TicketService(TicketRepository repository, TicketMapper mapper, ShowTimeRepository showTimeRepository, SeatRepository seatRepository, BookingRepository bookingRepository, CodeGenerator codeGenerator, CinemaHallRepository cinemaHallRepository) {
        this.repository = repository;
        this.mapper = mapper;
        this.showTimeRepository = showTimeRepository;
        this.seatRepository = seatRepository;
        this.bookingRepository = bookingRepository;
        this.codeGenerator = codeGenerator;
        this.cinemaHallRepository = cinemaHallRepository;
    }

    @Override
    public TicketResponseDto getEntityById(UUID id) {
        return mapper.toResponse(repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Ticket not found", new Exception("id"))));
    }

    @Override
    public List<TicketResponseDto> getAllEntity() {
        return repository.findAll().stream().map(mapper::toResponse).toList();
    }

    @Override
    public TicketResponseDto createEntity(CreateTicketDto dto) {
        TicketEntity entity = mapper.toEntity(dto);
        String ticketCode = codeGenerator.generateUniqueTicketCode();
        entity.setTicketCode(ticketCode);
        entity.setShowTime(showTimeRepository.findById(dto.getShowTimeId()).orElseThrow(() -> new EntityNotFoundException("Show Time not found", new Exception("id"))));
        entity.setSeat(seatRepository.findById(dto.getSeatId()).orElseThrow(() -> new EntityNotFoundException("Seat not found", new Exception("id"))));
        entity.setBooking(bookingRepository.findById(dto.getBookingId()).orElseThrow(() -> new EntityNotFoundException("Booking not found", new Exception("id"))));
        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public TicketResponseDto updateEntity(UUID id, UpdateTicketDto dto) {
        TicketEntity entity = repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Ticket not found", new Exception("id")));
        mapper.updateEntityFromDto(dto, entity);
        entity.setShowTime(showTimeRepository.findById(dto.getShowTimeId()).orElseThrow(() -> new EntityNotFoundException("Show Time not found", new Exception("id"))));
        entity.setSeat(seatRepository.findById(dto.getSeatId()).orElseThrow(() -> new EntityNotFoundException("Seat not found", new Exception("id"))));
        entity.setBooking(bookingRepository.findById(dto.getBookingId()).orElseThrow(() -> new EntityNotFoundException("Booking not found", new Exception("id"))));
        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public void deleteEntity(UUID id) {
        TicketEntity entity = repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Ticket not found", new Exception("id")));
        repository.delete(entity);
    }

    @Override
    public PageDto<TicketResponseDto> searchEntity(Specification<TicketEntity> spec, Pageable pageable) {
        return new PageDto<>(repository.findAll(spec, pageable).map(mapper::toResponse));
    }

    @Transactional
    public void createListTicketForShowTime(ShowTimeEntity showTimeEntity) {
        // Load lại CinemaHall để đảm bảo có thể truy cập danh sách ghế
        CinemaHallEntity cinemaHall = cinemaHallRepository.findById(showTimeEntity.getCinemaHall().getId())
                .orElseThrow(() -> new EntityNotFoundException("CinemaHall not found"));

        Set<SeatEntity> seats = cinemaHall.getSeats();
        if (seats.isEmpty()) {
            throw new RuntimeException("No seats found for CinemaHall ID: " + cinemaHall.getId());
        }

        List<TicketEntity> ticketEntities = seats.stream()
                .map(seat -> {
                    TicketEntity ticket = new TicketEntity();
                    ticket.setTicketCode(codeGenerator.generateUniqueTicketCode());
                    ticket.setShowTime(showTimeEntity);
                    ticket.setSeat(seat);
                    ticket.setPrice(seat.getPrice());
                    ticket.setTicketType(TicketType.valueOf(seat.getType().toString()));
                    ticket.setTicketStatus(TicketStatus.AVAILABLE);
                    return ticket;
                })
                .toList();

        try {
            repository.saveAll(ticketEntities);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create tickets", e);
        }
    }

    @Override
    public List<TicketResponseDto> getTicketsByShowTime(UUID showTimeId) {
        return repository.findAllByShowTimeId(showTimeId)
                .stream()
                .map(mapper::toResponse)
                .toList();
    }


}
