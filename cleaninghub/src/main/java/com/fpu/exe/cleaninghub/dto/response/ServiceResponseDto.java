package com.fpu.exe.cleaninghub.dto.response;

import com.fpu.exe.cleaninghub.entity.Category;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServiceResponseDto {
    private Integer id;
    private String name;
    private String description;
    private Double basePrice;
    private String status;
    private String img;
    private Category category;
}
