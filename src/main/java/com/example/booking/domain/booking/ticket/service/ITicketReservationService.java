package com.example.booking.domain.booking.ticket.service;

import java.util.UUID;

public interface ITicketReservationService {

    void reserveTicket(UUID showTimeId, UUID ticketId, UUID userId);

    boolean isTicketReserved(UUID showTimeId, UUID ticketId);

    void releaseTicket(UUID showTimeId, UUID ticketId);
}
