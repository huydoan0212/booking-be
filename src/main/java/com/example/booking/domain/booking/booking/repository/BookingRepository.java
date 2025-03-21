package com.example.booking.domain.booking.booking.repository;

import com.example.booking.common.template.repository.CRUDRepository;
import com.example.booking.domain.booking.booking.entity.BookingEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingRepository extends CRUDRepository<BookingEntity> {
    boolean existsByBookingCode(String code);
}
