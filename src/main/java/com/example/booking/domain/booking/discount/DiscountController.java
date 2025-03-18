package com.example.booking.domain.booking.discount;

import com.example.booking.common.pagination.PageDto;
import com.example.booking.common.pagination.PageOptionsDto;
import com.example.booking.common.template.CRUDController;
import com.example.booking.domain.booking.discount.dto.CreateDiscountDto;
import com.example.booking.domain.booking.discount.dto.DiscountResponseDto;
import com.example.booking.domain.booking.discount.dto.UpdateDiscountDto;
import com.example.booking.domain.booking.discount.entity.DiscountEntity;
import com.example.booking.domain.booking.discount.service.IDiscountService;
import com.turkraft.springfilter.converter.FilterSpecification;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "/discount", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Discount")
public class DiscountController implements CRUDController<DiscountEntity, CreateDiscountDto, UpdateDiscountDto, DiscountResponseDto> {

    private final IDiscountService service;

    public DiscountController(IDiscountService service) {
        this.service = service;
    }

    @Override
    public DiscountResponseDto getById(UUID id) {
        return service.getEntityById(id);
    }

    @Override
    public List<DiscountResponseDto> getAll() {
        return service.getAllEntity();
    }

    @Override
    public void delete(UUID id) {
        service.deleteEntity(id);
    }

    @Override
    public DiscountResponseDto save(CreateDiscountDto dto) {
        return service.createEntity(dto);
    }

    @Override
    public DiscountResponseDto update(UUID id, UpdateDiscountDto dto) {
        return service.updateEntity(id, dto);
    }

    @Override
    public PageDto<DiscountResponseDto> search(FilterSpecification<DiscountEntity> spec, PageOptionsDto dto) {
        return service.searchEntity(spec, dto.toPageable());
    }
}
