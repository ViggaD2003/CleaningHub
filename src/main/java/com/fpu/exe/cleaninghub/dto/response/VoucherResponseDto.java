package com.fpu.exe.cleaninghub.dto.response;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class VoucherResponseDto {
    private Integer id;
    private Integer percentage;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;
    private LocalDateTime expiredDate;
    private Integer amount;
}
