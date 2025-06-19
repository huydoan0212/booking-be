package com.example.booking.domain.booking.ticket.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CheckTicketResponse {
    private boolean isOk;
    private UUID ticketId;
}
