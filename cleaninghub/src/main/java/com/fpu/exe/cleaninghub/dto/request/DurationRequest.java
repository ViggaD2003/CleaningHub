package com.fpu.exe.cleaninghub.dto.request;

import jakarta.persistence.Column;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DurationRequest {
    private Double area;
    private Integer durationInHours;
    private Double price;
    private Integer serviceId;
}
