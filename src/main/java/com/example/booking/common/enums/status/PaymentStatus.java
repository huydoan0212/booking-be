package com.example.booking.common.enums.status;

public enum PaymentStatus {
    PENDING,   // Chờ thanh toán
    SUCCESS,   // Thanh toán thành công
    FAILED,    // Thanh toán thất bại
    EXPIRED    // Hết hạn (không thanh toán trong thời gian quy định)
}
