package com.example.booking.domain.vnpay.service;

import com.example.booking.domain.vnpay.dto.PaymentResponse;

import java.io.UnsupportedEncodingException;

public interface IVnPayService {
    PaymentResponse createOrder(String orderId, long price) throws UnsupportedEncodingException;

    void setStatusOrder(String orderId, String status) throws UnsupportedEncodingException;

    boolean notifyOrder(String vnp_ResponseCode, String vnp_TxnRef, String vnp_TransactionNo, String vnp_TransactionDate, String vnp_Amount);
}
