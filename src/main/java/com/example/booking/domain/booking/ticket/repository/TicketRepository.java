package com.example.booking.domain.booking.ticket.repository;

import com.example.booking.common.template.repository.CRUDRepository;
import com.example.booking.domain.booking.ticket.entity.TicketEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Repository
public interface TicketRepository extends CRUDRepository<TicketEntity> {

    @Transactional
    @Query("SELECT COUNT(t) > 0 FROM TicketEntity t WHERE t.ticketCode = :code")
    boolean existsByTicketCode(String code);

    @Query("SELECT t FROM TicketEntity t WHERE t.showTime.id = :showTimeId order by t.seat.seatRow, t.seat.seatColumn")
    List<TicketEntity> findAllByShowTimeId(UUID showTimeId);
}
