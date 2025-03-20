package com.example.booking.domain.booking.ticket.repository;

import com.example.booking.common.template.repository.CRUDRepository;
import com.example.booking.domain.booking.ticket.entity.TicketEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketRepository extends CRUDRepository<TicketEntity> {
}
