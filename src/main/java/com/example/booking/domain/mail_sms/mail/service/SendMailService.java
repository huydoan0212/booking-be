package com.example.booking.domain.mail_sms.mail.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.nio.charset.StandardCharsets;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class SendMailService {

    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;

    public SendMailService(JavaMailSender mailSender, SpringTemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }

    public void sendEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }

    /**
     * Gửi email HTML theo template booking_confirmation.html
     */
    public void sendBookingConfirmation(
            String to,
            String customerName,
            String cinemaName,
            OffsetDateTime bookingTime,
            String bookingCode,
            String qrCodeUrl,
            OffsetDateTime showTime
    ) throws MessagingException {
        // 1. Tạo context và đổ biến
        Context ctx = new Context();
        ctx.setVariable("customerName", customerName);
        ctx.setVariable("cinemaName", cinemaName);
        ctx.setVariable("bookingTime", bookingTime.format(DateTimeFormatter.ofPattern("HH:mm:ss dd/MM/yyyy")));
        ctx.setVariable("bookingCode", bookingCode);
        ctx.setVariable("qrCodeUrl", qrCodeUrl);
        ctx.setVariable("showTime", showTime.format(DateTimeFormatter.ofPattern("HH:mm:ss dd/MM/yyyy")));

        // 2. Render HTML
        String htmlContent = templateEngine.process("booking_confirmation", ctx);

        // 3. Tạo Mime message
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(
                message,
                MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                StandardCharsets.UTF_8.name()
        );
        helper.setTo(to);
        helper.setSubject("Xác nhận đặt vé MoMo – mã " + bookingCode);
        helper.setText(htmlContent, true);

        // 4. Gửi
        mailSender.send(message);
    }
}
