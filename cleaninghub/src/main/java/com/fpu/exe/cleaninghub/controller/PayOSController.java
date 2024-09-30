package com.fpu.exe.cleaninghub.controller;

import com.fpu.exe.cleaninghub.entity.Booking;
import com.fpu.exe.cleaninghub.entity.BookingDetail;
import com.fpu.exe.cleaninghub.entity.Payments;
import com.fpu.exe.cleaninghub.enums.Payment.PaymentStatus;
import com.fpu.exe.cleaninghub.repository.BookingRepository;
import com.fpu.exe.cleaninghub.services.interfc.BookingService;
import com.fpu.exe.cleaninghub.utils.wapper.API;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.util.UriComponentsBuilder;
import vn.payos.PayOS;
import vn.payos.type.CheckoutResponseData;
import vn.payos.type.ItemData;
import vn.payos.type.PaymentData;

import java.math.BigDecimal;
import java.net.http.HttpClient;
import java.util.Date;

@RestController
@RequestMapping("/api/v1/payOS")
@RequiredArgsConstructor
public class PayOSController {
    private final PayOS payOS;
    private final BookingRepository bookingRepository;
    private final BookingService bookingService;
    @PostMapping()
    public ResponseEntity<?> checkOut(@RequestParam Integer userId, @RequestParam Integer bookingId) {
        try {
            Booking booking = bookingRepository.findById(bookingId).orElseThrow(() -> new IllegalArgumentException("Booking not found"));
            Payments payments = booking.getBookingDetail().getPayment();

            String productName = booking.getService().getName();
            String description = "Thanh toan don hang";
            String baseUrl = "http://localhost:8080/api/v1/payOS";
            String url = UriComponentsBuilder.fromHttpUrl(baseUrl)
                    .queryParam("userId", userId)
                    .queryParam("bookingId", bookingId).toUriString();
            BigDecimal price = payments.getFinalPrice();
            String currentTimeString = String.valueOf(new Date().getTime());
            long orderCode = Long.parseLong(currentTimeString.substring(currentTimeString.length() - 6));
            ItemData item = ItemData.builder().name(productName).quantity(1).price(price.intValue()).build();
            PaymentData paymentData = PaymentData.builder().orderCode(orderCode).amount(price.intValue()).description(description)
                    .returnUrl(url).cancelUrl(url).item(item).build();
            CheckoutResponseData data = payOS.createPaymentLink(paymentData);

            String checkoutUrl = data.getCheckoutUrl();
            return ResponseEntity.status(HttpStatus.CREATED).body(API.Response.success(checkoutUrl));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(API.Response.error(HttpStatus.BAD_REQUEST, "Something went wrong", e.getMessage()));
        }
    }
    @GetMapping
    public RedirectView successOrCancel(@RequestParam Integer userId, @RequestParam Integer bookingId, @RequestParam String status){
        if (status.equalsIgnoreCase("cancel")){
            bookingService.changePaymentStatusOfBooking(bookingId, PaymentStatus.FAILED);
            return new RedirectView("");
        }
        bookingService.changePaymentStatusOfBooking(bookingId, PaymentStatus.SUCCESS);
        return new RedirectView("");
    }
}
