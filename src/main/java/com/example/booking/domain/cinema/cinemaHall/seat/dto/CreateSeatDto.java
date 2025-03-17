package com.example.booking.domain.cinema.cinemaHall.seat.dto;

import com.example.booking.common.enums.status.SeatStatus;
import com.example.booking.common.enums.type.SeatType;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class CreateSeatDto {
    private String seatRow;
    private int seatColumn;
    private SeatType type;
    private SeatStatus status;
    private UUID cinemaHallId;
}
