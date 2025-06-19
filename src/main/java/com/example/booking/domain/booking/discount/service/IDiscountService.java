package com.example.booking.domain.booking.discount.service;

import com.example.booking.common.template.service.ICRUDService;
import com.example.booking.domain.booking.discount.dto.CreateDiscountDto;
import com.example.booking.domain.booking.discount.dto.DiscountResponseDto;
import com.example.booking.domain.booking.discount.dto.UpdateDiscountDto;
import com.example.booking.domain.booking.discount.entity.DiscountEntity;

public interface IDiscountService extends ICRUDService<DiscountEntity, CreateDiscountDto, UpdateDiscountDto, DiscountResponseDto> {
}
