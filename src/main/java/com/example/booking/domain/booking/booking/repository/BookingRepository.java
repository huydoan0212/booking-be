package com.example.booking.domain.booking.booking.repository;

import com.example.booking.common.enums.status.PaymentStatus;
import com.example.booking.common.template.repository.CRUDRepository;
import com.example.booking.domain.booking.booking.entity.BookingEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends CRUDRepository<BookingEntity> {
    boolean existsByBookingCode(String code);


    List<BookingEntity> findByPaymentStatusAndCreatedAtBefore(
            PaymentStatus paymentStatus,
            OffsetDateTime cutoff
    );
}
