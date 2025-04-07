package com.example.booking.domain.movie.movie.dto;

import com.example.booking.domain.movie.category.dto.CategoryResponseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class MovieResponseDto {
    private UUID id;
    private String name;
    private int age;
    private int duration;
    private String imageLandscape;
    private String imagePortrait;
    private String slug;
    private double rate;
    private int totalVotes;
    private int views;
    private String description;
    private int sortOrder;
    private String actors;
    private String director;
    private String producers;
    private String country;
    private OffsetDateTime startDate;
    private OffsetDateTime endDate;
    private List<CategoryResponseDto> categories;
    private OffsetDateTime createdAt;
}
