package com.example.booking.domain.booking.discount.repository;

import com.example.booking.common.template.repository.CRUDRepository;
import com.example.booking.domain.booking.discount.entity.DiscountEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface DiscountRepository extends CRUDRepository<DiscountEntity> {
}
