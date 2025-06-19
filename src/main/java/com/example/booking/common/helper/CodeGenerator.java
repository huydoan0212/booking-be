package com.example.booking.common.helper;

import java.security.SecureRandom;
import com.example.booking.domain.booking.booking.repository.BookingRepository;
import com.example.booking.domain.booking.ticket.repository.TicketRepository;
import org.springframework.stereotype.Service;

@Service
public class CodeGenerator {

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int TICKET_CODE_LENGTH = 6;
    private static final int BOOKING_CODE_LENGTH = 8;
    private static final SecureRandom RANDOM = new SecureRandom();

    private final TicketRepository ticketRepository;
    private final BookingRepository bookingRepository;

    public CodeGenerator(TicketRepository ticketRepository, BookingRepository bookingRepository) {
        this.ticketRepository = ticketRepository;
        this.bookingRepository = bookingRepository;
    }

    /**
     * Tạo mã vé (ticket code) độc nhất, gồm cả chữ và số.
     *
     * @return mã vé duy nhất
     */
    public String generateUniqueTicketCode() {
        String code;
        do {
            code = generateRandomCode(TICKET_CODE_LENGTH);
        } while (ticketRepository.existsByTicketCode(code));
        return code;
    }

    /**
     * Tạo mã đặt chỗ (booking code) độc nhất, gồm cả chữ và số.
     *
     * @return mã đặt chỗ duy nhất
     */
    public String generateUniqueBookingCode() {
        String code;
        do {
            code = generateRandomCode(BOOKING_CODE_LENGTH);
        } while (bookingRepository.existsByBookingCode(code));
        return code;
    }

    /**
     * Tạo mã ngẫu nhiên gồm cả chữ và số với độ dài xác định.
     *
     * @param length độ dài mã cần tạo
     * @return mã ngẫu nhiên
     */
    private String generateRandomCode(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int index = RANDOM.nextInt(CHARACTERS.length());
            sb.append(CHARACTERS.charAt(index));
        }
        return sb.toString();
    }
}
