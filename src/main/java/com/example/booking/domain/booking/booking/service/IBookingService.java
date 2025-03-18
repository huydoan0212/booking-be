package com.example.booking.domain.booking.booking.service;

import com.example.booking.common.template.service.ICRUDService;
import com.example.booking.domain.booking.booking.dto.BookingResponseDto;
import com.example.booking.domain.booking.booking.dto.CreateBookingDto;
import com.example.booking.domain.booking.booking.dto.UpdateBookingDto;
import com.example.booking.domain.booking.booking.entity.BookingEntity;

public interface IBookingService extends ICRUDService<BookingEntity, CreateBookingDto, UpdateBookingDto, BookingResponseDto> {
}
