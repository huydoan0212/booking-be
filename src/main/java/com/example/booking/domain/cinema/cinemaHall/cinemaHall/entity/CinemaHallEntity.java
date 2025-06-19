package com.example.booking.domain.cinema.cinemaHall.cinemaHall.entity;

import com.example.booking.domain.cinema.cinema.entity.CinemaEntity;
import com.example.booking.domain.cinema.cinemaHall.seat.entity.SeatEntity;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.core.annotation.Order;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.OffsetDateTime;
import java.util.Set;
import java.util.UUID;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "cinema_halls")
@EntityListeners(AuditingEntityListener.class)
public class CinemaHallEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "name")
    private String name;

    @Column(name = "total_seats")
    private int totalSeats;

    @Column(name = "screen_type")
    private String screenType;

    @Column(name = "sound_system")
    private String soundSystem;

    @ManyToOne
    @JoinColumn(name = "cinema_id")
    private CinemaEntity cinema;

    @OrderBy("seatRow ASC, seatColumn ASC")
    @OneToMany(mappedBy = "cinemaHall", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private Set<SeatEntity> seats;

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
