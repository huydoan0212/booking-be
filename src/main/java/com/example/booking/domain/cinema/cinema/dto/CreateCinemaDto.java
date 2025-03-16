package com.example.booking.domain.cinema.cinema.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CreateCinemaDto {
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
