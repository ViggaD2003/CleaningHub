package com.fpu.exe.cleaninghub.dto.response;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class VoucherResponse {

    private Integer id;

    private Integer percentage;

    private LocalDate createDate;

    private LocalDate updateDate;

    private LocalDateTime expiredDate;

    private Integer amount;
}
