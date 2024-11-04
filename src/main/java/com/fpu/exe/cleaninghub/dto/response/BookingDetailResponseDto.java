package com.fpu.exe.cleaninghub.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
@Data
@NoArgsConstructor
public class BookingDetailResponseDto {
    private Integer id;
    private String serviceName;
    private List<String> staffName;
    private String address;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    private VoucherResponseDto voucher;
    private PaymentResponseDto payment;
}

