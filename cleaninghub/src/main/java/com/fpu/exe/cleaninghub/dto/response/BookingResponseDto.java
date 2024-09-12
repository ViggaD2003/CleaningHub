package com.fpu.exe.cleaninghub.dto.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingResponseDto {
    private Integer id;
    private String status;
    private LocalDate bookingDate;
    private String address;
    private String serviceName;
    private String staffName;
}
