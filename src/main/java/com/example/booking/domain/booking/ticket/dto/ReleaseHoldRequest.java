package com.example.booking.domain.booking.ticket.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class ReleaseHoldRequest {
    private UUID showTimeId;
    private UUID userId;
}
