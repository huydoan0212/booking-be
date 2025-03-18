package com.example.booking.domain.booking.booking.dto;

import com.example.booking.common.enums.status.BookingStatus;
import com.example.booking.common.enums.status.PaymentStatus;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class CreateBookingDto {
    private String bookingCode;
    private double totalPrice;
    private double finalPrice;
    private UUID discountId;
    private UUID userId;
    private BookingStatus bookingStatus;
    private PaymentStatus paymentStatus;
}
