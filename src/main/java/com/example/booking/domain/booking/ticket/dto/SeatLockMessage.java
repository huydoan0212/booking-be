package com.example.booking.domain.booking.ticket.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SeatLockMessage {
    private UUID showTimeId;
    private UUID ticketId;
    private UUID userId;
    private double price;
    private String seatLabel;
    private String seatType;
}

