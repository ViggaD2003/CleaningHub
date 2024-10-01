package com.fpu.exe.cleaninghub.controller;

import com.fpu.exe.cleaninghub.enums.Payment.PaymentStatus;
import com.fpu.exe.cleaninghub.services.interfc.BookingService;
import com.fpu.exe.cleaninghub.services.interfc.PayOsService;
import com.fpu.exe.cleaninghub.utils.wapper.API;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import vn.payos.type.CheckoutResponseData;


@RestController
@RequestMapping("/api/v1/payOS")
@RequiredArgsConstructor
public class PayOSController {
    private final PayOsService payOsService;
    private final BookingService bookingService;
    private Long orderCode;
    @PostMapping()
    public ResponseEntity<?> checkOut(HttpServletRequest request, @RequestParam Integer bookingId) {
        try {
            CheckoutResponseData data = payOsService.createCheckoutUrl(request, bookingId);
            orderCode = data.getOrderCode();
            return ResponseEntity.status(HttpStatus.CREATED).body(API.Response.success(data.getCheckoutUrl()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(API.Response.error(HttpStatus.BAD_REQUEST, "Something went wrong", e.getMessage()));
        }
    }
    @GetMapping
    public RedirectView successOrCancel(@RequestParam Integer bookingId, @RequestParam String status){
        if (status.equalsIgnoreCase("CANCELLED")){
            bookingService.changePaymentStatusOfBooking(orderCode, bookingId, PaymentStatus.FAILED);
            return new RedirectView("https://www.facebook.com");
        }
        bookingService.changePaymentStatusOfBooking(orderCode, bookingId, PaymentStatus.SUCCESS);
        return new RedirectView("https://www.facebook.com");
    }
}
