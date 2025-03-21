package com.example.booking.domain.booking.booking.dto;

import com.example.booking.common.enums.status.BookingStatus;
import com.example.booking.common.enums.status.PaymentStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateBookingDto {
    private BookingStatus bookingStatus;
    private PaymentStatus paymentStatus;
}
