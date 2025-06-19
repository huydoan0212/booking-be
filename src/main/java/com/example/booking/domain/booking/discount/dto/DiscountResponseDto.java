package com.example.booking.domain.booking.discount.dto;

import com.example.booking.common.enums.type.DiscountType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class DiscountResponseDto {
    private UUID id;
    private String code;
    private String description;
    private DiscountType discountType;
    private double discountValue;
    private int usageLimit;
    private OffsetDateTime startDate;
    private OffsetDateTime endDate;
    private OffsetDateTime createdAt;
}
