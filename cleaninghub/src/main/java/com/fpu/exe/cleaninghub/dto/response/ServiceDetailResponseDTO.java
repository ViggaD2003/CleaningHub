package com.fpu.exe.cleaninghub.dto.response;

import com.fpu.exe.cleaninghub.entity.Category;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ServiceDetailResponseDTO {
    private Integer id;
    private String name;
    private String description;
    private Double basePrice;
    private String status;
    private Category category;
}
