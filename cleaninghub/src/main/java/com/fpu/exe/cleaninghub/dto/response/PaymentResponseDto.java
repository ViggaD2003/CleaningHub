package com.fpu.exe.cleaninghub.dto.response;

import com.fpu.exe.cleaninghub.enums.Payment.PaymentMethod;
import com.fpu.exe.cleaninghub.enums.Payment.PaymentStatus;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class PaymentResponseDto {
    private Integer id;
    private Double finalPrice;
    private PaymentStatus paymentStatus;
    private PaymentMethod paymentMethod;
    private String transactionId;
    private LocalDateTime createdDate;
}
