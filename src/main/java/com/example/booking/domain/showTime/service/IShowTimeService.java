package com.example.booking.domain.showTime.service;

import com.example.booking.common.template.service.ICRUDService;
import com.example.booking.domain.showTime.dto.CreateShowTimeDto;
import com.example.booking.domain.showTime.dto.ShowTimeResponseDto;
import com.example.booking.domain.showTime.dto.UpdateShowTimeDto;
import com.example.booking.domain.showTime.entity.ShowTimeEntity;

public interface IShowTimeService extends ICRUDService<ShowTimeEntity, CreateShowTimeDto, UpdateShowTimeDto, ShowTimeResponseDto> {
}
