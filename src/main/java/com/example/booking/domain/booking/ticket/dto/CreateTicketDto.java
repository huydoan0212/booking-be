package com.example.booking.domain.booking.ticket.dto;

import com.example.booking.common.enums.status.TicketStatus;
import com.example.booking.common.enums.type.TicketType;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class CreateTicketDto {
    private UUID showTimeId;
    private double price;
    private TicketType ticketType;
    private TicketStatus ticketStatus;
    private UUID seatId;
    private UUID bookingId;
}
