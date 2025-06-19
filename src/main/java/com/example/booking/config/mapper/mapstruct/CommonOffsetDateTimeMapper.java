package com.example.booking.config.mapper.mapstruct;

import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@Component
public class CommonOffsetDateTimeMapper {
    public OffsetDateTime toUtc7(OffsetDateTime dateTime) {
        return dateTime == null ? null : dateTime.withOffsetSameInstant(ZoneOffset.of("+07:00"));
    }
}
