package com.fpu.exe.cleaninghub.dto.response;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DurationResponse {
    private Integer id;

    private Double area;

    private Integer numberOfWork;

    private Integer durationInHours;

    private Double price;
}
