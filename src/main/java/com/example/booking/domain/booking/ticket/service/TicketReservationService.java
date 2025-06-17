package com.example.booking.domain.booking.ticket.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.ReturnType;
import org.springframework.data.redis.core.*;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class TicketReservationService implements ITicketReservationService {

    private static final long HOLD_EXPIRE_SECONDS = 420; // 7 phút

    // Key templates
    private static final String HOLD_KEY_FMT = "hold:show:%s:user:%s";
    private static final String HOLD_HASH_FMT = "hold:show:%s:user:%s:tickets";
    private static final String LOCK_KEY_FMT = "lock:show:%s:ticket:%s";
    private static final String HOLD_KEY_PATTERN = "hold:show:%s:user:*";

    private final RedisTemplate<String, String> redisTemplate;
    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public TicketReservationService(RedisTemplate<String, String> redisTemplate, SimpMessagingTemplate messagingTemplate) {
        this.redisTemplate = redisTemplate;
        this.messagingTemplate = messagingTemplate;
    }

    /**
     * Initialize or keep holdKey with TTL.
     */
    public void startHold(UUID showtimeId, UUID userId) {
        String holdKey = String.format(HOLD_KEY_FMT, showtimeId, userId);
        if (Boolean.TRUE.equals(redisTemplate.hasKey(holdKey))) return;
        redisTemplate.opsForValue().set(holdKey, userId.toString(), Duration.ofSeconds(HOLD_EXPIRE_SECONDS));
    }

    /**
     * Lock ticket and store ticketId|price|seatLabel|seatType in Redis hash.
     */
    public boolean lockSeat(UUID showtimeId, UUID ticketId, UUID userId, double price, String seatLabel, String seatType) {
        startHold(showtimeId, userId);
        String lockKey = String.format(LOCK_KEY_FMT, showtimeId, ticketId);
        Boolean success = redisTemplate.opsForValue().setIfAbsent(lockKey, userId.toString(), Duration.ofSeconds(HOLD_EXPIRE_SECONDS));
        if (Boolean.TRUE.equals(success)) {
            String holdHashKey = String.format(HOLD_HASH_FMT, showtimeId, userId);
            String value = String.join("|", ticketId.toString(), String.valueOf(price), seatLabel, seatType);
            redisTemplate.opsForHash().put(holdHashKey, ticketId.toString(), value);
            redisTemplate.expire(holdHashKey, Duration.ofSeconds(HOLD_EXPIRE_SECONDS));
            return true;
        }
        return false;
    }

    /**
     * Unlock seat: remove field from hash to preserve other holds.
     */
    public boolean unlockSeat(UUID showtimeId, UUID ticketId, UUID userId) {
        // Tên các key
        String lockKey = String.format(LOCK_KEY_FMT, showtimeId, ticketId);
        String holdHashKey = String.format(HOLD_HASH_FMT, showtimeId, userId);

        // Chỉ cho phép unlock nếu chính user này đã lock (Lua script)
        String lua = ""
                + "if redis.call('get', KEYS[1]) == ARGV[1] then "
                + "  return 1 "
                + "else "
                + "  return 0 "
                + "end";

        RedisCallback<Long> callback = conn -> conn.eval(
                lua.getBytes(StandardCharsets.UTF_8),
                ReturnType.INTEGER,
                1,
                lockKey.getBytes(StandardCharsets.UTF_8),
                userId.toString().getBytes(StandardCharsets.UTF_8)
        );
        Long result = redisTemplate.execute(callback);
        if (result != null && result > 0) {
            // Xóa entry trong Hash để bỏ thông tin vé đã hold
            redisTemplate.opsForHash().delete(holdHashKey, ticketId.toString());

            // Xóa luôn lockKey để giải phóng vé
            redisTemplate.delete(lockKey);
// **Broadcast UNLOCKED để FE cập nhật UI ngay**
            messagingTemplate.convertAndSend(
                    "/topic/seat-status/" + showtimeId,
                    Map.of(
                            "ticketId", ticketId,
                            "status", "AVAILABLE",
                            "userId", userId
                    )
            );
            // Lưu ý: không xóa holdKey, nên TTL của phiên giữ vé vẫn chạy tiếp
            return true;
        }
        return false;
    }


    /**
     * Get all held tickets with price, label, seatType, plus secondsRemaining.
     */
    // 2) Cập nhật getAllHolds
    public AllHoldInfo getAllHolds(UUID showtimeId, UUID userId) {
        String myHashKey = String.format(HOLD_HASH_FMT, showtimeId, userId);

        // --- heldTickets của chính user ---
        Map<Object, Object> myEntries = redisTemplate.opsForHash().entries(myHashKey);
        List<HeldTicket> heldTickets = new ArrayList<>();
        for (Object val : myEntries.values()) {
            heldTickets.add(parseHeldTicket(val.toString()));
        }

        // TTL của phiên giữ chỗ
        String holdKey = String.format(HOLD_KEY_FMT, showtimeId, userId);
        Long ttl = redisTemplate.getExpire(holdKey, TimeUnit.SECONDS);
        long secondsRemaining = ttl != null && ttl > 0 ? ttl : 0;

        // --- otherHeldTickets: scan tất cả hash keys của showtime ---
        String pattern = String.format(HOLD_HASH_FMT, showtimeId, "*");
        Set<String> allKeys = redisTemplate.keys(pattern);
        List<HeldTicket> otherHeld = new ArrayList<>();

        if (allKeys != null) {
            for (String hashKey : allKeys) {
                if (hashKey.equals(myHashKey)) continue;  // bỏ qua chính user
                Map<Object, Object> entries = redisTemplate.opsForHash().entries(hashKey);
                for (Object val : entries.values()) {
                    otherHeld.add(parseHeldTicket(val.toString()));
                }
            }
        }

        return new AllHoldInfo(heldTickets, secondsRemaining, otherHeld);
    }

    // Helper method để parse một value "ticketId|price|label|type"
    private HeldTicket parseHeldTicket(String csv) {
        String[] parts = csv.split("\\|");
        UUID tId       = UUID.fromString(parts[0]);
        double price   = Double.parseDouble(parts[1]);
        String label   = parts[2];
        String type    = parts[3];
        return new HeldTicket(tId, price, label, type);
    }

    /**
     * Lấy owner hiện tại (userId) của khóa ghế, hoặc null nếu chưa có khóa.
     */
    public UUID getLockOwner(UUID showtimeId, UUID ticketId) {
        String lockKey = String.format(LOCK_KEY_FMT, showtimeId, ticketId);
        String value = redisTemplate.opsForValue().get(lockKey);
        if (value == null) return null;
        try {
            return UUID.fromString(value);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    /**
     * Scheduled cleanup expired holds and notify timeout.
     */
    @Scheduled(fixedDelay = 10000)
    public void checkExpiredHolds() {
        // Chỉ scan hash keys (nơi giữ thông tin vé)
        String pattern = String.format(HOLD_HASH_FMT, "*", "*");
        ScanOptions opts = ScanOptions.scanOptions()
                .match(pattern)
                .count(100)
                .build();

        try (Cursor<byte[]> cursor =
                     (Cursor<byte[]>) redisTemplate.getConnectionFactory()
                             .getConnection()
                             .scan(opts)) {

            while (cursor.hasNext()) {
                String hashKey = new String(cursor.next(), StandardCharsets.UTF_8);
                Long ttl = redisTemplate.getExpire(hashKey, TimeUnit.SECONDS);
                // TTL <= 0 => đã hết hoặc tự xóa
                if (ttl == null || ttl <= 0) {
                    // hashKey format: hold:show:{showtimeId}:user:{userId}:tickets
                    String[] parts = hashKey.split(":");
                    UUID showtimeId = UUID.fromString(parts[2]);
                    UUID userId     = UUID.fromString(parts[4]);
                    releaseAllHolds(showtimeId, userId);
                }
            }
        } catch (Exception ignored) {}
    }


    public void releaseAllHolds(UUID showTimeId, UUID userId) {
        String holdHashKey = String.format(HOLD_HASH_FMT, showTimeId, userId);
        Map<Object, Object> entries = redisTemplate.opsForHash().entries(holdHashKey);
        for (Object val : entries.values()) {
            String[] parts = val.toString().split("\\|");
            UUID ticketId = UUID.fromString(parts[0]);
            String lockKey = String.format(LOCK_KEY_FMT, showTimeId, ticketId);
            redisTemplate.delete(lockKey);
            messagingTemplate.convertAndSend("/topic/seat-status/" + showTimeId, Map.of("ticketId", ticketId, "status", "UNLOCK_TIMEOUT", "userId", userId));
        }
        String holdKey = String.format(HOLD_KEY_FMT, showTimeId, userId);
        redisTemplate.delete(holdHashKey);
        redisTemplate.delete(holdKey);
    }

    public record HeldTicket(UUID ticketId, double price, String seatLabel, String seatType) {
    }

    public record AllHoldInfo(List<HeldTicket> heldTickets, long secondsRemaining, List<HeldTicket> otherHeldTickets) {
    }
}
