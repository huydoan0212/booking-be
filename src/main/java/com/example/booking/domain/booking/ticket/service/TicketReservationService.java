package com.example.booking.domain.booking.ticket.service;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class TicketReservationService implements ITicketReservationService {

    private static final long RESERVATION_TTL_SECONDS = 300; // 5 phút

    private final RedisTemplate<String, String> redisTemplate;

    public TicketReservationService(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    private String getTicketKey(UUID showTimeId, UUID ticketId) {
        return "ticket:" + showTimeId + ":" + ticketId;
    }

    // Đặt giữ vé với TTL 5 phút
    public void reserveTicket(UUID showTimeId, UUID ticketId, UUID userId) {
        String key = getTicketKey(showTimeId, ticketId);
        redisTemplate.opsForValue().set(key, userId.toString(), RESERVATION_TTL_SECONDS, TimeUnit.SECONDS);
    }

    // Kiểm tra xem vé có đang được giữ không
    public boolean isTicketReserved(UUID showTimeId, UUID ticketId) {
        String key = getTicketKey(showTimeId, ticketId);
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }

    // Giải phóng vé khỏi trạng thái giữ
    public void releaseTicket(UUID showTimeId, UUID ticketId) {
        String key = getTicketKey(showTimeId, ticketId);
        redisTemplate.delete(key);
    }
}
