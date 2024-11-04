package com.fpu.exe.cleaninghub.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.jmx.export.annotation.ManagedNotifications;

@Getter
@Setter
@NoArgsConstructor
public class DurationResponseDTO {
    private Integer id;

    private Double area;

    private Integer numberOfWork;

    private Integer durationInHours;

    private Double price;
}
