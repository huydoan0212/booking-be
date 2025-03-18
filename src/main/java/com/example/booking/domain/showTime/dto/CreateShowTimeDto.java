package com.example.booking.domain.showTime.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.UUID;

@Setter
@Getter
public class CreateShowTimeDto {
    private UUID movieId;
    private UUID cinemaHallId;
    @Schema(example = "2025-03-18T14:00:00+07:00")
    private OffsetDateTime showTime;
    private String language;
    private String subtitle;
    private String screenFormat;
}
