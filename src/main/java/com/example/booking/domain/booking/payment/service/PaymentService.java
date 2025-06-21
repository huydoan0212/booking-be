package com.example.booking.domain.booking.payment.service;

import com.example.booking.common.enums.status.BookingStatus;
import com.example.booking.common.enums.status.PaymentStatus;
import com.example.booking.common.enums.status.TicketStatus;
import com.example.booking.config.vnpay.VNPayConfig;
import com.example.booking.domain.booking.booking.entity.BookingEntity;
import com.example.booking.domain.booking.booking.repository.BookingRepository;
import com.example.booking.domain.booking.payment.dto.PaymentNotification;
import com.example.booking.domain.booking.payment.dto.PaymentResponse;
import com.example.booking.domain.booking.ticket.repository.TicketRepository;
import com.example.booking.domain.booking.ticket.service.TicketReservationService;
import com.example.booking.domain.cinema.cinema.entity.CinemaEntity;
import com.example.booking.domain.mail_sms.mail.service.SendMailService;
import com.example.booking.domain.showTime.entity.ShowTimeEntity;
import jakarta.mail.MessagingException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class PaymentService implements IPaymentService {

    private final BookingRepository bookingRepository;
    private final TicketRepository ticketRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private final TicketReservationService ticketReservationService;
    private final SendMailService sendMailService;

    public PaymentService(BookingRepository bookingRepository, TicketRepository ticketRepository, SimpMessagingTemplate messagingTemplate, TicketReservationService ticketReservationService, SendMailService sendMailService) {
        this.bookingRepository = bookingRepository;
        this.ticketRepository = ticketRepository;
        this.messagingTemplate = messagingTemplate;
        this.ticketReservationService = ticketReservationService;
        this.sendMailService = sendMailService;
    }

    @Override
    public PaymentResponse createPayment(UUID booking) throws UnsupportedEncodingException {
        BookingEntity bookingEntity = bookingRepository.findById(booking).orElseThrow(() -> new EntityNotFoundException("Booking not found"));
        String vnp_Version = "2.1.0";
        String vnp_Command = "pay";
        String orderType = "other";
        long amount = (long) (bookingEntity.getFinalPrice() * 100);
        String bankCode = "NCB";

        String vnp_TxnRef = String.valueOf(bookingEntity.getId());
        String vnp_IpAddr = "192.168.0.42";

        String vnp_TmnCode = VNPayConfig.vnp_TmnCode;

        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", vnp_Version);
        vnp_Params.put("vnp_Command", vnp_Command);
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(amount));
        vnp_Params.put("vnp_CurrCode", "VND");

        if (bankCode != null && !bankCode.isEmpty()) {
            vnp_Params.put("vnp_BankCode", bankCode);
        }
        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", "Thanh toan don hang:" + vnp_TxnRef);
        vnp_Params.put("vnp_OrderType", orderType);

        vnp_Params.put("vnp_Locale", "vn");
        vnp_Params.put("vnp_ReturnUrl", VNPayConfig.vnp_ReturnUrl);
        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);

        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

        cld.add(Calendar.MINUTE, 15);
        String vnp_ExpireDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

        List fieldNames = new ArrayList(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        Iterator itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = (String) itr.next();
            String fieldValue = (String) vnp_Params.get(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                //Build hash data
                hashData.append(fieldName);
                hashData.append('=');
                hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                //Build query
                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
                query.append('=');
                query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                if (itr.hasNext()) {
                    query.append('&');
                    hashData.append('&');
                }
            }
        }
        String queryUrl = query.toString();
        String vnp_SecureHash = VNPayConfig.hmacSHA512(VNPayConfig.secretKey, hashData.toString());
        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
        String paymentUrl = VNPayConfig.vnp_PayUrl + "?" + queryUrl;
        PaymentResponse response = new PaymentResponse();
        response.setUrl(paymentUrl);
        return response;
    }

    @Override
    public void setStatusBooking(UUID booking, PaymentStatus status, BookingStatus bookingStatus) throws UnsupportedEncodingException {
        BookingEntity bookingEntity = bookingRepository.findById(booking).orElseThrow(() -> new EntityNotFoundException("Booking not found"));
        bookingEntity.setPaymentStatus(status);
        bookingEntity.setBookingStatus(bookingStatus);
        bookingEntity.getTickets().forEach(ticket -> {
            ticket.setTicketStatus(TicketStatus.BOOKED);
            ticketRepository.save(ticket);
        });
        bookingRepository.save(bookingEntity);
    }

    @Override
    public boolean notifyBooking(String vnp_ResponseCode, String vnp_TxnRef, String vnp_TransactionNo, String vnp_TransactionDate, String vnp_Amount) throws UnsupportedEncodingException, MessagingException {
        boolean success = "00".equals(vnp_ResponseCode);
        BookingEntity bookingEntity = bookingRepository.findById(UUID.fromString(vnp_TxnRef)).orElseThrow(() -> new EntityNotFoundException("Booking not found"));
        if (success) {
            setStatusBooking(UUID.fromString(vnp_TxnRef), PaymentStatus.SUCCESS, BookingStatus.PAID);
            onBookingSuccess(bookingEntity, "hello");
        } else {
            setStatusBooking(UUID.fromString(vnp_TxnRef), PaymentStatus.FAILED, BookingStatus.CANCELLED);
        }
        AtomicReference<UUID> showTimeId = new AtomicReference<>();
        bookingEntity.getTickets().forEach(ticket -> {
            showTimeId.set(ticket.getShowTime().getId());
        });
        ticketReservationService.releaseAllHolds(showTimeId.get(), bookingEntity.getUser().getId());
        // Chuẩn bị payload
        PaymentNotification payload = new PaymentNotification();
        payload.setTxnRef(vnp_TxnRef);
        payload.setSuccess(success);
        payload.setMessage(success ? "Thanh toán thành công" : "Thanh toán thất bại");

        // Gửi lên topic chung: /topic/payment-status/{txnRef}
        messagingTemplate.convertAndSend(
                "/topic/payment-status/" + vnp_TxnRef,
                payload
        );

        return success;
    }

    public PaymentResponse createPayment(double price) throws UnsupportedEncodingException {
//        BookingEntity bookingEntity = bookingRepository.findById(booking).orElseThrow(() -> new EntityNotFoundException("Booking not found"));
        String vnp_Version = "2.1.0";
        String vnp_Command = "pay";
        String orderType = "other";
        long amount = (long) (price * 100);
        String bankCode = "NCB";

        String vnp_TxnRef = "1";
        String vnp_IpAddr = "192.168.0.42";

        String vnp_TmnCode = VNPayConfig.vnp_TmnCode;

        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", vnp_Version);
        vnp_Params.put("vnp_Command", vnp_Command);
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(amount));
        vnp_Params.put("vnp_CurrCode", "VND");

        if (bankCode != null && !bankCode.isEmpty()) {
            vnp_Params.put("vnp_BankCode", bankCode);
        }
        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", "Thanh toan don hang:" + vnp_TxnRef);
        vnp_Params.put("vnp_OrderType", orderType);

        vnp_Params.put("vnp_Locale", "vn");
        vnp_Params.put("vnp_ReturnUrl", VNPayConfig.vnp_ReturnUrl);
        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);

        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

        cld.add(Calendar.MINUTE, 15);
        String vnp_ExpireDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

        List fieldNames = new ArrayList(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        Iterator itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = (String) itr.next();
            String fieldValue = (String) vnp_Params.get(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                //Build hash data
                hashData.append(fieldName);
                hashData.append('=');
                hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                //Build query
                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
                query.append('=');
                query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                if (itr.hasNext()) {
                    query.append('&');
                    hashData.append('&');
                }
            }
        }
        String queryUrl = query.toString();
        String vnp_SecureHash = VNPayConfig.hmacSHA512(VNPayConfig.secretKey, hashData.toString());
        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
        String paymentUrl = VNPayConfig.vnp_PayUrl + "?" + queryUrl;
        PaymentResponse response = new PaymentResponse();
        response.setUrl(paymentUrl);
        return response;
    }

    public void onBookingSuccess(BookingEntity booking, String qrCodeUrl) throws MessagingException {
        AtomicReference<CinemaEntity> cinema = new AtomicReference<>(new CinemaEntity());
        AtomicReference<ShowTimeEntity> showtime = new AtomicReference<>(new ShowTimeEntity());
        booking.getTickets().forEach(ticket -> {
            cinema.set(ticket.getShowTime().getCinemaHall().getCinema());
            showtime.set(ticket.getShowTime());
        });
        sendMailService.sendBookingConfirmation(
                booking.getUser().getUsername(),
                booking.getUser().getName(),
                cinema.get().getName(),
                booking.getCreatedAt(),
                booking.getBookingCode(),
                qrCodeUrl,
                showtime.get().getShowTime()
        );
    }
}
