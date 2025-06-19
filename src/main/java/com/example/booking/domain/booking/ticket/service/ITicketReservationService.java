package com.example.booking.domain.booking.ticket.service;

import java.util.UUID;

public interface ITicketReservationService {

    boolean lockSeat(UUID showtimeId,
                            UUID ticketId,
                            UUID userId,
                            double price,
                            String seatLabel,
                            String seatType);

    boolean unlockSeat(UUID showtimeId, UUID ticketId, UUID userId);

    UUID getLockOwner(UUID showtimeId, UUID ticketId);

    TicketReservationService.AllHoldInfo getAllHolds(UUID showtimeId, UUID userId);

    void releaseAllHolds(UUID showtimeId, UUID userId);
}
