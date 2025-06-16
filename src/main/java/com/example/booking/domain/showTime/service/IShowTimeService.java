package com.example.booking.domain.showTime.service;

import com.example.booking.common.template.service.ICRUDService;
import com.example.booking.domain.showTime.dto.CreateShowTimeDto;
import com.example.booking.domain.showTime.dto.ShowTimeResponseDto;
import com.example.booking.domain.showTime.dto.UpdateShowTimeDto;
import com.example.booking.domain.showTime.entity.ShowTimeEntity;

import java.util.List;
import java.util.UUID;

public interface IShowTimeService extends ICRUDService<ShowTimeEntity, CreateShowTimeDto, UpdateShowTimeDto, ShowTimeResponseDto> {
    boolean createShowTimes(UUID movieId, List<CreateShowTimeDto> createShowTimeDtos);
}
