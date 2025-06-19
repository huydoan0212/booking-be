package com.example.booking.domain.booking.booking.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class CreateBookingDto {
//    private UUID discountId;
    private UUID userId;
    private List<UUID> ticketIds;
}
