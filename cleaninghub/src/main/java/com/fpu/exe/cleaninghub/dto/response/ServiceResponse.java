package com.fpu.exe.cleaninghub.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ServiceResponse {
    private Integer id;
    private String name;
    private String description;
    private Double basePrice;
    private String status;
}
