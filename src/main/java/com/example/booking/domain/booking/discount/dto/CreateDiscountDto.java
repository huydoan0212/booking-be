package com.example.booking.domain.booking.discount.dto;

import com.example.booking.common.enums.type.DiscountType;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;

@Getter
@Setter
public class CreateDiscountDto {
    private String code;
    private String description;
    private DiscountType discountType;
    private double discountValue;
    private OffsetDateTime startDate;
    private OffsetDateTime endDate;
    private int usageLimit;
}
