package com.example.booking.domain.booking.booking.service;

import com.example.booking.common.pagination.PageDto;
import com.example.booking.domain.booking.booking.dto.BookingResponseDto;
import com.example.booking.domain.booking.booking.dto.CreateBookingDto;
import com.example.booking.domain.booking.booking.dto.UpdateBookingDto;
import com.example.booking.domain.booking.booking.entity.BookingEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class BookingService implements IBookingService{
    @Override
    public BookingResponseDto getEntityById(UUID id) {
        return null;
    }

    @Override
    public List<BookingResponseDto> getAllEntity() {
        return List.of();
    }

    @Override
    public BookingResponseDto createEntity(CreateBookingDto dto) {
        return null;
    }

    @Override
    public BookingResponseDto updateEntity(UUID id, UpdateBookingDto dto) {
        return null;
    }

    @Override
    public void deleteEntity(UUID id) {

    }

    @Override
    public PageDto<BookingResponseDto> searchEntity(Specification<BookingEntity> spec, Pageable pageable) {
        return null;
    }
}
