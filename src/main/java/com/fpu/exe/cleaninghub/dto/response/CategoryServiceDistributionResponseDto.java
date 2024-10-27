package com.fpu.exe.cleaninghub.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CategoryServiceDistributionResponseDto {
    private String categoryName;
    private long serviceCount;
    private double percentage;
}
