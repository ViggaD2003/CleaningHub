package com.fpu.exe.cleaninghub.services.impl;

import com.fpu.exe.cleaninghub.dto.response.PaymentOverReviewDto;
import com.fpu.exe.cleaninghub.repository.PaymentRepository;
import com.fpu.exe.cleaninghub.services.interfc.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PaymentServiceImpl implements PaymentService {
    @Autowired
    private PaymentRepository paymentRepository;

    @Override
    public List<PaymentOverReviewDto> getPaymentOverReviews() {
        return paymentRepository.findAll().stream().map(payments -> new PaymentOverReviewDto(payments.getCreatedDate(), payments.getFinalPrice()))
                .collect(Collectors.toList());
    }
}
