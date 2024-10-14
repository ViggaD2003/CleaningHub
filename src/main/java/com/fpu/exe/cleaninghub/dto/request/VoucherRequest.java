package com.fpu.exe.cleaninghub.dto.request;

import com.fpu.exe.cleaninghub.entity.BookingDetail;
import jakarta.persistence.Column;
import jakarta.persistence.OneToOne;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDate;
import java.time.LocalDateTime;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class VoucherRequest {
    private Integer id;
    private Integer percentage;

    private LocalDateTime expiredDate;

    private Integer amount;
}
