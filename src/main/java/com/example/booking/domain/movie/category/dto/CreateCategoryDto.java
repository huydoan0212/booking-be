package com.example.booking.domain.movie.category.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateCategoryDto {
    private String name;
    private String slug;
    private String description;
}
