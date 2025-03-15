package com.example.booking.domain.movie.category.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CategoryResponseDto {
    private String id;
    private String name;
    private String slug;
    private String description;
}
