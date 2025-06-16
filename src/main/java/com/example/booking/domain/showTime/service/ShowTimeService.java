package com.example.booking.domain.showTime.service;

import com.example.booking.common.pagination.PageDto;
import com.example.booking.domain.booking.ticket.service.ITicketService;
import com.example.booking.domain.cinema.cinemaHall.cinemaHall.entity.CinemaHallEntity;
import com.example.booking.domain.cinema.cinemaHall.cinemaHall.repository.CinemaHallRepository;
import com.example.booking.domain.movie.movie.entity.MovieEntity;
import com.example.booking.domain.movie.movie.repository.MovieRepository;
import com.example.booking.domain.showTime.dto.CreateShowTimeDto;
import com.example.booking.domain.showTime.dto.ShowTimeResponseDto;
import com.example.booking.domain.showTime.dto.UpdateShowTimeDto;
import com.example.booking.domain.showTime.entity.ShowTimeEntity;
import com.example.booking.domain.showTime.mapper.ShowTimeMapper;
import com.example.booking.domain.showTime.repository.ShowTimeRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class ShowTimeService implements IShowTimeService {

    private final ShowTimeRepository repository;
    private final ShowTimeMapper mapper;
    private final MovieRepository movieRepository;
    private final CinemaHallRepository cinemaHallRepository;
    private final ITicketService ticketService;

    public ShowTimeService(ShowTimeRepository repository, ShowTimeMapper mapper, MovieRepository movieRepository, CinemaHallRepository cinemaHallRepository, ITicketService ticketService) {
        this.repository = repository;
        this.mapper = mapper;
        this.movieRepository = movieRepository;
        this.cinemaHallRepository = cinemaHallRepository;
        this.ticketService = ticketService;
    }


    @Override
    public ShowTimeResponseDto getEntityById(UUID id) {
        return mapper.toResponse(repository.findById(id).orElseThrow(() -> new EntityNotFoundException("ShowTime not found", new Exception("id"))));
    }

    @Override
    public List<ShowTimeResponseDto> getAllEntity() {
        return repository.findAll().stream().map(mapper::toResponse).toList();
    }

    @Transactional
    @Override
    public ShowTimeResponseDto createEntity(CreateShowTimeDto dto) {
        // Kiểm tra trùng lặp showtime
        if (repository.countOverlappingShowtimes(dto.getCinemaHallId(), dto.getShowTime()) > 0) {
            throw new IllegalArgumentException("2 showtimes in the same theater must be at least 2 hours apart from the nearest showtime!",
                    new Throwable("showTime"));
        }

        // Tìm movie và cinemaHall
        MovieEntity movie = movieRepository.findById(dto.getMovieId())
                .orElseThrow(() -> new EntityNotFoundException("Movie not found", new Exception("movieId")));

        CinemaHallEntity cinemaHall = cinemaHallRepository.findById(dto.getCinemaHallId())
                .orElseThrow(() -> new EntityNotFoundException("CinemaHall not found", new Exception("cinemaHallId")));

        // Tạo và lưu ShowTime
        ShowTimeEntity entity = mapper.toEntity(dto);
        entity.setMovie(movie);
        entity.setCinemaHall(cinemaHall);
        ShowTimeEntity finalEntity = repository.save(entity);

        // Gọi phương thức tạo ticket đồng bộ
        ticketService.createListTicketForShowTime(finalEntity);
        return mapper.toResponse(finalEntity);
    }


    @Override
    @Transactional
    public ShowTimeResponseDto updateEntity(UUID id, UpdateShowTimeDto dto) {
        long count = repository.countOverlappingShowtimes(dto.getCinemaHallId(), dto.getShowTime());
        if (count > 0) {
            throw new IllegalArgumentException("2 showtimes in the same theater must be at least 2 hours apart from the nearest showtime!", new Throwable("showTime"));
        }
        ShowTimeEntity entity = repository.findById(id).orElseThrow(() -> new EntityNotFoundException("ShowTime not found", new Exception("id")));
        mapper.updateEntityFromDto(dto, entity);
        entity.setMovie(movieRepository.findById(dto.getMovieId()).orElseThrow(() -> new EntityNotFoundException("Movie not found", new Exception("movieId"))));
        entity.setCinemaHall(cinemaHallRepository.findById(dto.getCinemaHallId()).orElseThrow(() -> new EntityNotFoundException("CinemaHall not found", new Exception("cinemaHallId"))));
        return mapper.toResponse(repository.save(entity));
    }

    @Transactional
    @Override
    public void deleteEntity(UUID id) {
        ShowTimeEntity entity = repository.findById(id).orElseThrow(() -> new EntityNotFoundException("ShowTime not found", new Exception("id")));
        repository.delete(entity);
    }

    @Override
    public PageDto<ShowTimeResponseDto> searchEntity(Specification<ShowTimeEntity> spec, Pageable pageable) {
        return new PageDto<>(repository.findAll(spec, pageable).map(mapper::toResponse));
    }

    @Transactional
    @Override
    public boolean createShowTimes(UUID movieId, List<CreateShowTimeDto> createShowTimeDtos) {
        for (CreateShowTimeDto dto : createShowTimeDtos) {
            dto.setMovieId(movieId);
            ShowTimeResponseDto response = createEntity(dto);
            if(Objects.isNull(response)) {
                return false;
            }
        }
        return true;
    }
}
