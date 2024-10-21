package com.fpu.exe.cleaninghub.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DurationResponseDTO {
    private Integer id;

    private Double area;

    private Integer numberOfWork;

    private Integer durationInHours;

    private Double price;
}
