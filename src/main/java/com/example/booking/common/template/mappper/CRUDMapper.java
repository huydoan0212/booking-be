package com.example.booking.common.template.mappper;

public interface CRUDMapper<E, C, U, R> {

    R toResponse(E entity);

    E toEntity(C dto);

    void updateEntityFromDto(U dto, E entity);

}
