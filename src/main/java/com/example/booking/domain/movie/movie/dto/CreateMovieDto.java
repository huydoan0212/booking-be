package com.example.booking.domain.movie.movie.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Schema(description = "DTO for creating a movie")
public class CreateMovieDto {

    @Schema(description = "Movie name", example = "Inception")
    private String name;

    @Schema(description = "Minimum age required to watch", example = "16")
    private int age;

    @Schema(description = "Movie duration in minutes", example = "148")
    private int duration;

    @Schema(description = "Landscape image URL", example = "https://example.com/inception-landscape.jpg")
    private String imageLandscape;

    @Schema(description = "Portrait image URL", example = "https://example.com/inception-portrait.jpg")
    private String imagePortrait;

    @Schema(description = "Movie slug for SEO-friendly URL", example = "inception")
    private String slug;

    @Schema(description = "Movie rating", example = "8.8")
    private double rate;

    @Schema(description = "Total number of votes", example = "2000000")
    private int totalVotes;

    @Schema(description = "Number of views", example = "10000000")
    private int views;

    @Schema(description = "Movie description", example = "A mind-bending thriller by Christopher Nolan.")
    private String description;

    @Schema(description = "Display order", example = "1")
    private int sortOrder;

    @Schema(description = "List of actors", example = "Leonardo DiCaprio, Joseph Gordon-Levitt")
    private String actors;

    @Schema(description = "Director name", example = "Christopher Nolan")
    private String director;

    @Schema(description = "Producers", example = "Emma Thomas, Christopher Nolan")
    private String producers;

    @Schema(description = "Country of origin", example = "USA")
    private String country;

    @Schema(description = "Trailer", example = "https://example.com/inception-trailer.mp4")
    private String trailer;

    @Schema(description = "Status", example = "1")
    private int status;

    @Schema(description = "Movie start date", example = "2025-06-15T00:00:00Z")
    private OffsetDateTime startDate;

    @Schema(description = "Movie end date", example = "2025-12-31T00:00:00Z")
    private OffsetDateTime endDate;

    @Schema(description = "List of category IDs", example = "[\"550e8400-e29b-41d4-a716-446655440000\"]")
    private List<UUID> categoryIds;
}
