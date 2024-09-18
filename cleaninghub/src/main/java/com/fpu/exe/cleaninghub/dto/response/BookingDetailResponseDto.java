package com.fpu.exe.cleaninghub.dto.response;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class BookingDetailResponseDto {
    private Integer id;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    private VoucherResponseDto voucher;
    private PaymentResponseDto payment;
}
