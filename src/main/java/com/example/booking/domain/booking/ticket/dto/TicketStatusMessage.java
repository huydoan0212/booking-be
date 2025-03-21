package com.example.booking.domain.booking.ticket.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TicketStatusMessage {
    private UUID ticketId;
    private String status; // "AVAILABLE", "RESERVED", "BOOKED"
}
