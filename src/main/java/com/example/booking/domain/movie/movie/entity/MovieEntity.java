package com.example.booking.domain.movie.movie.entity;

import com.example.booking.domain.movie.category.entity.CategoryEntity;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "movies")
@EntityListeners(AuditingEntityListener.class)
public class MovieEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "name")
    private String name;

    @Column(name = "age")
    private int age;

    @Column(name = "duration")
    private int duration;

    @Column(name = "image_landscape")
    private String imageLandscape;

    @Column(name = "image_portrait")
    private String imagePortrait;

    @Column(name = "slug")
    private String slug;

    @Column(name = "rate")
    private double rate;

    @Column(name = "total_votes")
    private int totalVotes;

    @Column(name = "views")
    private int views;

    @Column(name = "description")
    private String description;

    @Column(name = "sort_order")
    private int sortOrder;

    @Column(name = "actors")
    private String actors;

    @Column(name = "director")
    private String director;

    @Column(name = "producers")
    private String producers;

    @Column(name = "country")
    private String country;

    @Column(name = "trailer")
    private String trailer;

    @Column(name = "status")
    private int status;

    @Column(name = "start_date")
    private OffsetDateTime startDate;

    @Column(name = "end_date")
    private OffsetDateTime endDate;

    @ManyToMany
    @JoinTable(
            name = "movie_categories",
            joinColumns = @JoinColumn(name = "movie_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private Set<CategoryEntity> categories;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false, nullable = false)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private OffsetDateTime createdAt;

    @CreatedBy
    @Column(name = "created_by")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private UUID createdBy;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private OffsetDateTime updatedAt;

    @LastModifiedBy
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name = "updated_by")
    private UUID updatedBy;

}
