package com.fpu.exe.cleaninghub.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateServiceResponseDto {
    private Integer id;
    private String name;
    private String description;
    private Double basePrice;
    private String status;
    private String img;
    private String categoryName;
}
