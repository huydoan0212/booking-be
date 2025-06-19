package com.example.booking.domain.cinema.cinemaHall.seat.entity;

import com.example.booking.common.enums.status.SeatStatus;
import com.example.booking.common.enums.type.SeatType;
import com.example.booking.domain.cinema.cinemaHall.cinemaHall.entity.CinemaHallEntity;
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
import java.util.UUID;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "seats")
@EntityListeners(AuditingEntityListener.class)
public class SeatEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "seat_row")
    private String seatRow;

    @Column(name = "seat_column")
    private int seatColumn;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private SeatType type;

    @Column(name = "price")
    private int price;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private SeatStatus status;

    @ManyToOne
    @JoinColumn(name = "cinema_hall_id")
    private CinemaHallEntity cinemaHall;

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

    public SeatEntity(String seatRow, int seatColumn, SeatType type, SeatStatus status, CinemaHallEntity cinemaHall) {
        this.seatRow = seatRow;
        this.seatColumn = seatColumn;
        this.type = type;
        this.status = status;
        this.cinemaHall = cinemaHall;
    }

    public SeatEntity(String seatRow, int seatColumn, SeatType type, SeatStatus status, int price, CinemaHallEntity cinemaHall, UUID createdBy, UUID updatedBy) {
        this.seatRow = seatRow;
        this.seatColumn = seatColumn;
        this.type = type;
        this.price = price;
        this.status = status;
        this.cinemaHall = cinemaHall;
        this.createdBy = createdBy;
        this.updatedBy = updatedBy;
    }
}
