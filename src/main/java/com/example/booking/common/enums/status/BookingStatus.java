package com.example.booking.common.enums.status;

public enum BookingStatus {
    PENDING_PAYMENT,   // Đang chờ thanh toán
    PAID,              // Đã thanh toán thành công
    CANCELLED          // Hủy do không thanh toán hoặc thanh toán thất bại
}
