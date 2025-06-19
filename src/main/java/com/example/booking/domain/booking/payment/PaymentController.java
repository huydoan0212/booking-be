package com.example.booking.domain.booking.payment;

import com.example.booking.domain.booking.payment.dto.PaymentResponse;
import com.example.booking.domain.booking.payment.service.PaymentService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.UUID;

@RestController
@RequestMapping(path = "/payment", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Payment")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/")
    public PaymentResponse createBooking(@RequestParam UUID booking) throws UnsupportedEncodingException {
        return paymentService.createPayment(booking);
    }

    @GetMapping("/notify")
    public String notify(HttpServletResponse response,
                         @RequestParam String vnp_Amount,
                         @RequestParam String vnp_BankCode,
                         @RequestParam(required = false) String vnp_BankTranNo,
                         @RequestParam String vnp_CardType,
                         @RequestParam String vnp_OrderInfo,
                         @RequestParam String vnp_PayDate,
                         @RequestParam String vnp_ResponseCode,
                         @RequestParam String vnp_TmnCode,
                         @RequestParam String vnp_TransactionNo,
                         @RequestParam String vnp_TransactionStatus,
                         @RequestParam String vnp_TxnRef,
                         @RequestParam String vnp_SecureHash) throws UnsupportedEncodingException {
        boolean isSuccess = paymentService.notifyBooking(vnp_ResponseCode, vnp_TxnRef, vnp_TransactionNo, vnp_PayDate, vnp_Amount);
        return isSuccess ? "Thanh toán thành công" : "Thanh toán thất bại";
    }

    @PostMapping("/test")
    public PaymentResponse createBooking(@RequestParam double booking) throws UnsupportedEncodingException {
        return paymentService.createPayment(booking);
    }
}
