package com.fpu.exe.cleaninghub.dto.response;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class BookingDetailResponseDto {
    private Integer id;
    private LocalDate createDate;
    private LocalDate updateDate;
    private VoucherResponseDto voucher;
    private BookingResponseDto booking;
    private List<PaymentResponseDto> payments;
}
