package com.example.booking.domain.booking.ticket.repository;

import com.example.booking.common.template.repository.CRUDRepository;
import com.example.booking.domain.booking.ticket.entity.TicketEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface TicketRepository extends CRUDRepository<TicketEntity> {

    @Transactional
    @Query("SELECT COUNT(t) > 0 FROM TicketEntity t WHERE t.ticketCode = :code")
    boolean existsByTicketCode(String code);

}
