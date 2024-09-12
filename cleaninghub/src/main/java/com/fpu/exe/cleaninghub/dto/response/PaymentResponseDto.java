package com.fpu.exe.cleaninghub.dto.response;

import lombok.Data;

import java.time.LocalDate;

@Data
public class PaymentResponseDto {
    private Integer id;
    private Double finalPrice;
    private LocalDate createDate;
}
