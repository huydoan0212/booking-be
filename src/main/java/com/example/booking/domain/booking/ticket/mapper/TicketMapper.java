package com.example.booking.domain.booking.ticket.mapper;

import com.example.booking.common.template.mappper.CRUDMapper;
import com.example.booking.config.mapper.mapstruct.CentralMapperConfig;
import com.example.booking.domain.booking.ticket.dto.CreateTicketDto;
import com.example.booking.domain.booking.ticket.dto.TicketResponseDto;
import com.example.booking.domain.booking.ticket.dto.UpdateTicketDto;
import com.example.booking.domain.booking.ticket.entity.TicketEntity;
import org.mapstruct.Mapper;

@Mapper(config = CentralMapperConfig.class)
public interface TicketMapper extends CRUDMapper<TicketEntity, CreateTicketDto, UpdateTicketDto, TicketResponseDto> {
}
