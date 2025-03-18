package com.example.booking.domain.booking.booking.dto;

import com.example.booking.common.enums.status.BookingStatus;
import com.example.booking.common.enums.status.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.OffsetDateTime;
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
    private OffsetDateTime createdAt;
}
