package com.fpu.exe.cleaninghub.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class CreateServiceRequestDto {
    private String name;
    private String description;
    private Double basePrice;
    private String status;
    private Integer categoryId;
}
