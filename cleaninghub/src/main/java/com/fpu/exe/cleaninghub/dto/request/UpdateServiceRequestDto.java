package com.fpu.exe.cleaninghub.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateServiceRequestDto {
    private String name;
    private String description;
    private Double basePrice;
    private String status;
    private Integer categoryId;
}
