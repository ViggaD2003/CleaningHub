package com.fpu.exe.cleaninghub.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class PaymentOverReviewDto {
    private LocalDateTime createDate;
    private BigDecimal finalPrice;
}
