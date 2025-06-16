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

    // Key mẫu:
    //   holdKey       = "hold:show:{showtimeId}:user:{userId}"
    //   holdSetKey    = "hold:show:{showtimeId}:user:{userId}:tickets"
    //   lockKey       = "lock:show:{showtimeId}:ticket:{ticketId}"
    private static final String HOLD_KEY_FMT    = "hold:show:%s:user:%s";
    private static final String HOLD_SET_FMT    = "hold:show:%s:user:%s:tickets";
    private static final String LOCK_KEY_FMT    = "lock:show:%s:ticket:%s";
    private static final String HOLD_KEY_PATTERN = "hold:show:%s:user:*:tickets";

    private final RedisTemplate<String, String> redisTemplate;
    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public TicketReservationService(
            RedisTemplate<String, String> redisTemplate,
            SimpMessagingTemplate messagingTemplate) {
        this.redisTemplate = redisTemplate;
        this.messagingTemplate = messagingTemplate;
    }

    /**
     * Khởi tạo “hold key” 7 phút cho user.
     * Nếu holdKey đã tồn tại, giữ nguyên TTL, không reset lại.
     */
    public void startHold(UUID showtimeId, UUID userId) {
        String holdKey = String.format(HOLD_KEY_FMT, showtimeId, userId);
        Boolean exists = redisTemplate.hasKey(holdKey);
        if (Boolean.TRUE.equals(exists)) {
            // đã có hold key, không làm gì thêm
            return;
        }
        // Tạo key với value = userId và TTL = 7 phút
        redisTemplate.opsForValue().set(
                holdKey,
                userId.toString(),
                Duration.ofSeconds(HOLD_EXPIRE_SECONDS)
        );
    }

    /**
     * Thử lock ghế:
     *  1. Đảm bảo holdKey (startHold) đã được gọi trước ít nhất một lần.
     *  2. Tạo riêng lockKey cho ticket với TTL = 7 phút.
     *  3. Thêm ticketId vào Set để sau này dễ dọn dẹp.
     */
    public boolean lockSeat(UUID showtimeId, UUID ticketId, UUID userId) {
        // 1. Tạo hoặc giữ nguyên HOLD
        startHold(showtimeId, userId);

        // 2. Lock riêng cho ticket
        String lockKey = String.format(LOCK_KEY_FMT, showtimeId, ticketId);
        String value = userId.toString();
        Boolean success = redisTemplate.opsForValue()
                .setIfAbsent(lockKey, value, Duration.ofSeconds(HOLD_EXPIRE_SECONDS));
        if (Boolean.TRUE.equals(success)) {
            // 3. Nếu lock thành công, thêm ticketId vào Set
            String holdSetKey = String.format(HOLD_SET_FMT, showtimeId, userId);
            redisTemplate.opsForSet().add(holdSetKey, ticketId.toString());
            // Đặt TTL cho Set giống TTL của holdKey (nếu chưa có, expire thiết lập lại)
            redisTemplate.expire(holdSetKey, Duration.ofSeconds(HOLD_EXPIRE_SECONDS));
            return true;
        }
        return false;
    }

    /**
     * Thử unlock ghế (bỏ chọn):
     * Xóa riêng lockKey và remove ticketId khỏi Set.
     */
    public boolean unlockSeat(UUID showtimeId, UUID ticketId, UUID userId) {
        String lockKey = String.format(LOCK_KEY_FMT, showtimeId, ticketId);

        // Chỉ kiểm tra nếu key do user này giữ, KHÔNG xóa key
        String luaScript = "if redis.call('get', KEYS[1]) == ARGV[1] then " +
                " return 1 " + // Trả về 1 nếu hợp lệ, nhưng không xóa key
                "else return 0 end";

        RedisCallback<Long> callback = connection -> {
            byte[] rawKey = lockKey.getBytes(StandardCharsets.UTF_8);
            byte[] rawVal = userId.toString().getBytes(StandardCharsets.UTF_8);
            return connection.eval(
                    luaScript.getBytes(StandardCharsets.UTF_8),
                    ReturnType.INTEGER,
                    1,
                    rawKey,
                    rawVal
            );
        };

        Long result = redisTemplate.execute(callback);
        if (result != null && result > 0) {
            // Xóa ticketId khỏi Set
            String holdSetKey = String.format(HOLD_SET_FMT, showtimeId, userId);
            redisTemplate.opsForSet().remove(holdSetKey, ticketId.toString());

            // ❗Không xóa key lock => vẫn giữ thời gian giữ chỗ
            return true;
        }
        return false;
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
     * Scheduler chạy mỗi 10s để kiểm tra xem có holdKey nào đã hết TTL không.
     * Nếu đã hết, gọi releaseAllHolds để xóa tất cả lockKey của user đó,
     * broadcast “UNLOCK_TIMEOUT” cho từng ticketId đã lock, rồi xóa holdKey và Set.
     */
    @Scheduled(fixedDelay = 10000)
    public void checkExpiredHolds() {
        // Scan mọi key dạng “hold:show:*:user:*”
        ScanOptions options = ScanOptions.scanOptions()
                .match("hold:show:*:user:*")
                .count(100)
                .build();

        Cursor<byte[]> cursor = (Cursor<byte[]>) redisTemplate.getConnectionFactory()
                .getConnection()
                .scan(options);

        while (cursor.hasNext()) {
            String holdKey = new String(cursor.next(), StandardCharsets.UTF_8);
            Long ttl = redisTemplate.getExpire(holdKey);
            if (ttl == null || ttl == -2) {
                // holdKey đã hết TTL (Redis tự động xóa), ta cần dọn
                // holdKey format: “hold:show:{showtimeId}:user:{userId}”
                String[] parts = holdKey.split(":");
                // ["hold","show","{showtimeId}","user","{userId}"]
                if (parts.length == 5) {
                    UUID showtimeId = UUID.fromString(parts[2]);
                    UUID userId = UUID.fromString(parts[4]);
                    releaseAllHolds(showtimeId, userId);
                }
            }
        }
    }

    /**
     * Xóa toàn bộ lockKey của user này cho showtime này,
     * rồi broadcast “UNLOCK_TIMEOUT” cho từng ticket, sau đó xóa holdSet và holdKey.
     */
    private void releaseAllHolds(UUID showtimeId, UUID userId) {
        String holdSetKey = String.format(HOLD_SET_FMT, showtimeId, userId);
        Set<String> ticketIds = redisTemplate.opsForSet().members(holdSetKey);

        if (ticketIds != null) {
            for (String ticketIdStr : ticketIds) {
                UUID ticketId = UUID.fromString(ticketIdStr);
                // Xóa riêng lockKey
                String lockKey = String.format(LOCK_KEY_FMT, showtimeId, ticketId);
                redisTemplate.delete(lockKey);

                // Broadcast "UNLOCK_TIMEOUT" để front-end:
                //   - Nếu là user đó, redirect home
                //   - Mọi user khác thấy ghế trở về “AVAILABLE”
                messagingTemplate.convertAndSend(
                        "/topic/seat-status/" + showtimeId,
                        Map.of(
                                "ticketId", ticketId,
                                "status", "UNLOCK_TIMEOUT",
                                "userId", userId
                        )
                );
            }
        }

        // Xóa holdSet và holdKey
        String holdKey = String.format(HOLD_KEY_FMT, showtimeId, userId);
        redisTemplate.delete(holdSetKey);
        redisTemplate.delete(holdKey);
    }

    public AllHoldInfo getAllHolds(UUID showtimeId, UUID userId) {
        String pattern = String.format(HOLD_KEY_PATTERN, showtimeId);
        Set<String> keys = redisTemplate.keys(pattern);
        if (keys == null) keys = Collections.emptySet();

        List<String> heldByMe = new ArrayList<>();
        List<String> heldByOthers = new ArrayList<>();

        String myKey = String.format(HOLD_SET_FMT, showtimeId, userId);
        Long myTtl = redisTemplate.getExpire(myKey, TimeUnit.SECONDS);
        long secondsRemaining = (myTtl == null || myTtl < 0) ? 0L : myTtl;

        for (String key : keys) {
            Set<Object> members = Collections.singleton(redisTemplate.opsForSet().members(key));
            if (members == null) continue;
            List<String> ids = members.stream()
                    .map(Object::toString)
                    .toList();
            if (key.equals(myKey)) {
                heldByMe.addAll(ids);
            } else {
                heldByOthers.addAll(ids);
            }
        }
        heldByOthers = heldByOthers.stream().distinct().toList();

        return new AllHoldInfo(heldByMe, heldByOthers, secondsRemaining);
    }

    public record AllHoldInfo(
            List<String> heldByMe,
            List<String> heldByOthers,
            long secondsRemaining
    ) {}
}
