package com.example.booking.common.template.mappper;

import org.mapstruct.MappingTarget;

public interface CRUDMapper<E, C, U, R> {

    R toResponse(E entity);

    E toEntity(C dto);

    void updateEntityFromDto(U dto,@MappingTarget E entity);

}
