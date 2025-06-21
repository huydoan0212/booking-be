package com.example.booking.domain.booking.payment.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentNotification {
    private String txnRef;
    private boolean success;
    private String message;
}
