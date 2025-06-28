package com.example.booking.domain.cinema.cinema.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CreateCinemaDto {
    @NotNull
    @Min(10)
    private String name;
    private String slug;
    private double latitude;
    private double longitude;
    private String address;
    private String phone;
    private String imageLandscape;
    private String imagePortrait;
    private List<String> imgUrls;
    private int sortOrder;
}
