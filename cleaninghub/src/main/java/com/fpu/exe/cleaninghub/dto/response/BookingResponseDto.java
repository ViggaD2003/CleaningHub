package com.fpu.exe.cleaninghub.dto.response;


import com.fpu.exe.cleaninghub.entity.Address;
import com.fpu.exe.cleaninghub.enums.Booking.BookingStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingResponseDto {
    private Integer id;
    private BookingStatus status;
    private LocalDateTime bookingDate;
    private Address address;
    private String serviceName;
    private String staffName;
}