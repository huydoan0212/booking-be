package com.example.booking.domain.booking.ticket;

import com.example.booking.common.pagination.PageDto;
import com.example.booking.common.pagination.PageOptionsDto;
import com.example.booking.common.template.CRUDController;
import com.example.booking.domain.booking.ticket.dto.*;
import com.example.booking.domain.booking.ticket.entity.TicketEntity;
import com.example.booking.domain.booking.ticket.service.ITicketReservationService;
import com.example.booking.domain.booking.ticket.service.ITicketService;
import com.example.booking.domain.booking.ticket.service.TicketReservationService;
import com.turkraft.springfilter.converter.FilterSpecification;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping(path = "/ticket", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Ticket")
public class TicketController implements CRUDController<TicketEntity, CreateTicketDto, UpdateTicketDto, TicketResponseDto> {

    private static final Logger log = LoggerFactory.getLogger(TicketController.class);
    private final ITicketService service;
    private final SimpMessagingTemplate messagingTemplate;
    private final ITicketReservationService ticketReservationService;

    public TicketController(ITicketService service, SimpMessagingTemplate messagingTemplate, ITicketReservationService ticketReservationService) {
        this.service = service;
        this.messagingTemplate = messagingTemplate;
        this.ticketReservationService = ticketReservationService;
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

    @GetMapping("/showTime/{showTimeId}")
    public List<TicketResponseDto> getTicketsByShowTimeId(@PathVariable("showTimeId") UUID showTimeId) {
        return service.getTicketsByShowTime(showTimeId);
    }

    @GetMapping("/check-tickets")
    public CheckTicketResponse checkTickets(@RequestParam UUID showTime
            , @RequestParam List<UUID> ticketIds, @RequestParam UUID userId) {
        for (UUID ticketId : ticketIds) {
            UUID owner = ticketReservationService.getLockOwner(showTime, ticketId);
            if (owner != null && owner.equals(userId)) {
                return new CheckTicketResponse(false, ticketId);
            }
        }
        return new CheckTicketResponse(true, null);
    }

    /**
     * Client gửi message để lock ghế:
     * Destination: /app/lock-seat
     * Payload: { "showtimeId": "...", "ticketId": "...", "userId": "..." }
     */
    @MessageMapping("/lock-seat")
    public void lockSeat(SeatLockMessage msg, @Header("simpSessionId") String sessionId) {
        UUID showtimeId = msg.getShowTimeId();
        UUID ticketId = msg.getTicketId();
        UUID userId = msg.getUserId();
        double price = msg.getPrice();
        String seatLabel = msg.getSeatLabel();
        String seatType = msg.getSeatType();
        boolean locked = ticketReservationService.lockSeat(showtimeId, ticketId, userId, price, seatLabel, seatType);
        if (locked) {
            Map<String, Object> payload = new HashMap<>();
            payload.put("ticketId", ticketId);
            payload.put("status", "LOCKED");
            payload.put("userId", userId);
            messagingTemplate.convertAndSend(
                    "/topic/seat-status/" + showtimeId,
                    payload
            );
        } else {
            UUID owner = ticketReservationService.getLockOwner(showtimeId, ticketId);
            Map<String, Object> payload = new HashMap<>();
            payload.put("ticketId", ticketId);
            payload.put("status", "ALREADY_LOCKED");
            payload.put("owner", owner);
            messagingTemplate.convertAndSendToUser(
                    sessionId,
                    "/queue/errors",
                    payload
            );
        }
    }

    /**
     * Client gửi message để unlock ghế (ví dụ khi user bỏ chọn trước khi checkout hoặc sau khi checkout thành công):
     * Destination: /app/unlock-seat
     * Payload: { "showtimeId": "...", "ticketId": "...", "userId": "..." }
     */
    @MessageMapping("/unlock-seat")
    public void unlockSeat(SeatLockMessage msg) {
        UUID showtimeId = msg.getShowTimeId();
        UUID ticketId = msg.getTicketId();
        UUID userId = msg.getUserId();

        boolean unlocked = ticketReservationService.unlockSeat(showtimeId, ticketId, userId);
        if (unlocked) {
            Map<String, Object> payload = new HashMap<>();
            payload.put("ticketId", ticketId);
            payload.put("status", "UNLOCKED");
            messagingTemplate.convertAndSend(
                    "/topic/seat-status/" + showtimeId,
                    payload
            );
        }
    }

    @MessageMapping("/release-all-hold")
    public void releaseAllHolds(
            @Payload ReleaseHoldRequest req
    ) {
        ticketReservationService.releaseAllHolds(req.getShowTimeId(),
                req.getUserId());
    }

    @GetMapping("/hold/{showtimeId}/user/{userId}")
    public TicketReservationService.AllHoldInfo getAllHolds(
            @PathVariable("showtimeId") UUID showtimeId,
            @PathVariable("userId") UUID userId
    ) {
        return ticketReservationService.getAllHolds(showtimeId, userId);
    }
}
