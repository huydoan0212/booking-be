//package com.example.booking.common.helper;
//
//import java.security.SecureRandom;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//@Service
//public class TicketCodeGenerator {
//
//    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
//    private static final int CODE_LENGTH = 10; // có thể điều chỉnh độ dài mã
//    private static final SecureRandom RANDOM = new SecureRandom();
//
//    @Autowired
//    private TicketRepository ticketRepository; // Giả sử bạn có repository để kiểm tra mã trùng lặp
//
//    /**
//     * Tạo mã vé độc nhất. Nếu mã đã tồn tại trong DB, sẽ tạo lại.
//     * @return mã vé duy nhất
//     */
//    public String generateUniqueTicketCode() {
//        String code;
//        do {
//            code = generateRandomCode();
//        } while (ticketRepository.existsByTicketCode(code)); // kiểm tra mã đã tồn tại chưa
//        return code;
//    }
//
//    /**
//     * Tạo mã ngẫu nhiên gồm chữ in hoa và số.
//     * @return mã ngẫu nhiên
//     */
//    private String generateRandomCode() {
//        StringBuilder sb = new StringBuilder(CODE_LENGTH);
//        for (int i = 0; i < CODE_LENGTH; i++) {
//            int index = RANDOM.nextInt(CHARACTERS.length());
//            sb.append(CHARACTERS.charAt(index));
//        }
//        return sb.toString();
//    }
//}
//
