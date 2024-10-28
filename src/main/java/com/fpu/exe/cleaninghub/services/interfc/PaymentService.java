package com.fpu.exe.cleaninghub.services.interfc;

import com.fpu.exe.cleaninghub.dto.response.PaymentOverReviewDto;

import java.util.List;

public interface PaymentService {
    List<PaymentOverReviewDto> getPaymentOverReviews();
}
