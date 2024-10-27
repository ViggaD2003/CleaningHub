package com.fpu.exe.cleaninghub.controller;

import com.fpu.exe.cleaninghub.dto.response.PaymentOverReviewDto;
import com.fpu.exe.cleaninghub.services.interfc.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/payments")
public class PaymentController {
    @Autowired
    private PaymentService paymentService;
    @GetMapping("/over-review")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<PaymentOverReviewDto> overReview() {
        return paymentService.getPaymentOverReviews();
    }

}
