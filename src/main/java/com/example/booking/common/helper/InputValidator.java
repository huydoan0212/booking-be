package com.example.booking.common.helper;

public class InputValidator {

    // Regex cho email (đơn giản)
    private static final String EMAIL_REGEX = "^[\\w.-]+@(?:[\\w-]+\\.)+[\\w-]{2,4}$";
    // Regex cho số điện thoại 10 chữ số
    private static final String PHONE_REGEX = "^\\d{10}$";

    /**
     * Xác định loại của input.
     * @param input Chuỗi cần kiểm tra.
     * @return "email" nếu input là email, "phone" nếu là số điện thoại, "unknown" nếu không khớp.
     */
    public static String checkInputType(String input) {
        if (input == null) {
            return "unknown";
        }
        if (input.matches(EMAIL_REGEX)) {
            return "email";
        } else if (input.matches(PHONE_REGEX)) {
            return "phone";
        } else {
            return "unknown";
        }
    }

}

