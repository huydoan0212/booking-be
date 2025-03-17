package com.example.booking.domain.cinema.cinemaHall.seat.dto;

import com.example.booking.common.enums.status.SeatStatus;
import com.example.booking.common.enums.type.SeatType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class SeatResponseDto {
    private UUID id;
    private String seatRow;
    private int seatColumn;
    private SeatType type;
    private SeatStatus status;
    private OffsetDateTime createdAt;
}
