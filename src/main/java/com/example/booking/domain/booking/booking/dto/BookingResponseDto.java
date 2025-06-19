package com.example.booking.domain.booking.booking.dto;

import com.example.booking.common.enums.status.BookingStatus;
import com.example.booking.common.enums.status.PaymentStatus;
import com.example.booking.domain.booking.ticket.dto.TicketResponseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class BookingResponseDto {
    private UUID id;
    private String bookingCode;
    private double totalPrice;
    private double finalPrice;
    private UUID discountId;
    private UUID userId;
    private BookingStatus bookingStatus;
    private PaymentStatus paymentStatus;
    private List<TicketResponseDto> tickets;
    private OffsetDateTime createdAt;
}
