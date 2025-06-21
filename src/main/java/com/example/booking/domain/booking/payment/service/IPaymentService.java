package com.example.booking.domain.booking.payment.service;

import com.example.booking.common.enums.status.BookingStatus;
import com.example.booking.common.enums.status.PaymentStatus;
import com.example.booking.domain.booking.payment.dto.PaymentResponse;
import jakarta.mail.MessagingException;

import java.io.UnsupportedEncodingException;
import java.util.UUID;

public interface IPaymentService {
    PaymentResponse createPayment(UUID booking) throws UnsupportedEncodingException;

    void setStatusBooking(UUID booking, PaymentStatus status, BookingStatus bookingStatus) throws UnsupportedEncodingException;

    boolean notifyBooking(String vnp_ResponseCode, String vnp_TxnRef, String vnp_TransactionNo, String vnp_TransactionDate, String vnp_Amount) throws UnsupportedEncodingException, MessagingException;
}
