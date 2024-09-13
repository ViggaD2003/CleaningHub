package com.fpu.exe.cleaninghub.dto.response;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDate;

@Getter
@Setter
public class PaymentResponse {

    private Integer id;

    private Double finalPrice;

    private LocalDate createDate;
}
