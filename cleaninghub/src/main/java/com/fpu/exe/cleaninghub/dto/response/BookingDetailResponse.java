package com.fpu.exe.cleaninghub.dto.response;

import com.fpu.exe.cleaninghub.entity.Payments;
import com.fpu.exe.cleaninghub.entity.Voucher;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDate;

@Getter
@Setter
public class BookingDetailResponse {

    private Integer id;

    private LocalDate createDate;

    private LocalDate updateDate;

    private VoucherResponse voucher;

    private PaymentResponse payment;
}
