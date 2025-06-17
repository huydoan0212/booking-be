package com.example.booking.domain.vnpay.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentResponse {
    private String status;
    private String message;
    private String URL;
}
