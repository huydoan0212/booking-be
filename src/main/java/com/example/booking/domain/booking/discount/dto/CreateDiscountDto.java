package com.example.booking.domain.booking.discount.dto;

import com.example.booking.common.enums.type.DiscountType;
import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema(example = "2025-03-18T14:00:00+07:00")
    private OffsetDateTime startDate;
    @Schema(example = "2025-03-18T14:00:00+07:00")
    private OffsetDateTime endDate;
    private int usageLimit;
}
