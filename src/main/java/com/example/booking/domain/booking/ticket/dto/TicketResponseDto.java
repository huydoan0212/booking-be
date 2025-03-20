package com.example.booking.domain.booking.ticket.dto;

import com.example.booking.common.enums.status.TicketStatus;
import com.example.booking.common.enums.type.TicketType;
import com.example.booking.domain.booking.booking.dto.BookingResponseDto;
import com.example.booking.domain.booking.booking.entity.BookingEntity;
import com.example.booking.domain.cinema.cinemaHall.seat.dto.SeatResponseDto;
import com.example.booking.domain.cinema.cinemaHall.seat.entity.SeatEntity;
import com.example.booking.domain.showTime.dto.ShowTimeResponseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class TicketResponseDto {
    private UUID id;
    private String ticketCode;
    private ShowTimeResponseDto showTime;
    private double price;
    private TicketType ticketType;
    private TicketStatus ticketStatus;
    private SeatResponseDto seat;
    private BookingResponseDto booking;
    private OffsetDateTime createdAt;
}
