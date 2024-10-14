package com.fpu.exe.cleaninghub.dto.response;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class VoucherResponseDto {
    private Integer id;
    private Integer percentage;
    private LocalDate createDate;
    private LocalDate updateDate;
    private LocalDateTime expiredDate;
    private Integer amount;
}
