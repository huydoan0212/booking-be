package com.example.booking.domain.showTime.repository;

import com.example.booking.common.template.repository.CRUDRepository;
import com.example.booking.domain.showTime.entity.ShowTimeEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.UUID;

@Repository
public interface ShowTimeRepository extends CRUDRepository<ShowTimeEntity> {

    @Query(value = "SELECT COUNT(*) FROM show_times s " +
            "WHERE s.cinema_hall_id = :hallId " +
            "AND ABS(EXTRACT(EPOCH FROM (s.show_time - :newShowTime)) / 60) < 120",
            nativeQuery = true)
    long countOverlappingShowtimes(@Param("hallId") UUID hallId, @Param("newShowTime") OffsetDateTime newShowTime);

}
