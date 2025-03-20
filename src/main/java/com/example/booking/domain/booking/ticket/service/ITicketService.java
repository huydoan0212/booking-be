package com.example.booking.domain.booking.ticket.service;

import com.example.booking.common.template.service.ICRUDService;
import com.example.booking.domain.booking.ticket.dto.CreateTicketDto;
import com.example.booking.domain.booking.ticket.dto.TicketResponseDto;
import com.example.booking.domain.booking.ticket.dto.UpdateTicketDto;
import com.example.booking.domain.booking.ticket.entity.TicketEntity;

public interface ITicketService extends ICRUDService<TicketEntity, CreateTicketDto, UpdateTicketDto, TicketResponseDto> {
}
