package com.example.booking.domain.booking.booking.service;

import com.example.booking.common.enums.status.BookingStatus;
import com.example.booking.common.enums.status.PaymentStatus;
import com.example.booking.common.helper.CodeGenerator;
import com.example.booking.common.pagination.PageDto;
import com.example.booking.domain.booking.booking.dto.BookingResponseDto;
import com.example.booking.domain.booking.booking.dto.CreateBookingDto;
import com.example.booking.domain.booking.booking.dto.UpdateBookingDto;
import com.example.booking.domain.booking.booking.entity.BookingEntity;
import com.example.booking.domain.booking.booking.mapper.BookingMapper;
import com.example.booking.domain.booking.booking.repository.BookingRepository;
import com.example.booking.domain.booking.discount.entity.DiscountEntity;
import com.example.booking.domain.booking.discount.repository.DiscountRepository;
import com.example.booking.domain.booking.ticket.entity.TicketEntity;
import com.example.booking.domain.booking.ticket.repository.TicketRepository;
import com.example.booking.domain.user.user.entity.UserEntity;
import com.example.booking.domain.user.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.example.booking.common.enums.type.DiscountType.FIXED_AMOUNT;
import static com.example.booking.common.enums.type.DiscountType.PERCENTAGE;

@Service
public class BookingService implements IBookingService {

    private static final Logger log = LoggerFactory.getLogger(BookingService.class);
    private final BookingRepository repository;
    private final BookingMapper mapper;
    private final DiscountRepository discountRepository;
    private final UserRepository userRepository;
    private final CodeGenerator codeGenerator;
    private final TicketRepository ticketRepository;
    private final BookingRepository bookingRepository;

    public BookingService(BookingRepository repository, BookingMapper mapper, DiscountRepository discountRepository, UserRepository userRepository, CodeGenerator codeGenerator, TicketRepository ticketRepository, BookingRepository bookingRepository) {
        this.repository = repository;
        this.mapper = mapper;
        this.discountRepository = discountRepository;
        this.userRepository = userRepository;
        this.codeGenerator = codeGenerator;
        this.ticketRepository = ticketRepository;
        this.bookingRepository = bookingRepository;
    }

    @Override
    public BookingResponseDto getEntityById(UUID id) {
        return mapper.toResponse(repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Booking not found", new Exception("id"))));
    }

    @Override
    public List<BookingResponseDto> getAllEntity() {
        return repository.findAll().stream().map(mapper::toResponse).toList();
    }

    @Override
    @Transactional
    public BookingResponseDto createEntity(CreateBookingDto dto) {
        // Khởi tạo entity từ DTO
        BookingEntity entity = mapper.toEntity(dto);
        String bookingCode = codeGenerator.generateUniqueBookingCode();
        entity.setBookingCode(bookingCode);

        // Lấy thông tin User
        UserEntity user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User not found", new Exception("userId")));
        entity.setUser(user);

//        // Lấy thông tin Discount nếu có
        DiscountEntity discount = Optional.ofNullable(dto.getDiscountId())
                .flatMap(discountRepository::findById)
                .orElse(null);
        entity.setDiscount(discount);

        // Lấy danh sách vé từ DB
        List<TicketEntity> tickets = ticketRepository.findAllById(dto.getTicketIds());
        if (tickets.isEmpty()) {
            throw new IllegalArgumentException("No valid tickets found!");
        }

        // Tính tổng giá vé
        double totalPrice = tickets.stream().mapToDouble(TicketEntity::getPrice).sum();
        entity.setTotalPrice(totalPrice);

        // Gán vé vào booking: Nếu vé đã được đặt trước và booking trước đó bị hủy (CANCELLED), cho phép đặt lại
        tickets.forEach(ticket -> {
            if (ticket.getBooking() != null && ticket.getBooking().getBookingStatus() != BookingStatus.CANCELLED) {
                throw new IllegalArgumentException("Ticket is already booked!");
            }
            ticket.setBooking(entity);
        });

        // Tính giá sau khi áp dụng giảm giá (nếu có)
        double finalPrice = totalPrice;
        if (discount != null) {
            finalPrice = switch (discount.getDiscountType()) {
                case FIXED_AMOUNT -> Math.max(0, totalPrice - discount.getDiscountValue());
                case PERCENTAGE -> Math.max(0, totalPrice * (1 - discount.getDiscountValue() / 100.0));
            };
        }
        entity.setFinalPrice(finalPrice);

        // Thiết lập trạng thái ban đầu:
        // Khi mới tạo booking, người dùng chưa thanh toán, nên:
        entity.setBookingStatus(BookingStatus.PENDING_PAYMENT);
        entity.setPaymentStatus(PaymentStatus.PENDING);

        // Lưu booking và sau đó chuyển sang trang thanh toán
        return mapper.toResponse(repository.save(entity));
    }


    @Override
    public BookingResponseDto updateEntity(UUID id, UpdateBookingDto dto) {
        BookingEntity entity = repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Booking not found", new Exception("id")));
        mapper.updateEntityFromDto(dto, entity);
        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public void deleteEntity(UUID id) {
        BookingEntity entity = repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Booking not found", new Exception("id")));
        repository.delete(entity);
    }

    @Override
    public PageDto<BookingResponseDto> searchEntity(Specification<BookingEntity> spec, Pageable pageable) {
        return new PageDto<>(repository.findAll(spec, pageable).map(mapper::toResponse));
    }

    @Scheduled(cron = "0 * * * * *")  // mỗi phút, tại giây 0
    @Transactional
    public void cancelStaleBookings() {
        // Tính thời điểm cắt: bây giờ - 15 phút
        OffsetDateTime cutoff = OffsetDateTime.now(ZoneOffset.UTC).minusMinutes(15);

        // Lấy danh sách booking chưa thanh toán cũ hơn cutoff
        List<BookingEntity> stale = bookingRepository
                .findByPaymentStatusAndCreatedAtBefore(PaymentStatus.PENDING, cutoff);

        if (stale.isEmpty()) {
            log.debug("Không có booking cũ để huỷ");
            return;
        }

        log.info("Huỷ {} booking đã quá 15 phút chưa thanh toán", stale.size());

        for (BookingEntity b : stale) {
            b.setBookingStatus(BookingStatus.CANCELLED);
            b.setPaymentStatus(PaymentStatus.EXPIRED);
            // nếu cần, bạn có thể add logic notify qua WebSocket hoặc gửi email ở đây
        }

        // Lưu lại tất cả trong một batch
        bookingRepository.saveAll(stale);
        log.info("Hoàn tất huỷ booking cũ");
    }
}
