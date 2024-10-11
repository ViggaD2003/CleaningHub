package com.fpu.exe.cleaninghub.dto.response;


import com.fpu.exe.cleaninghub.enums.Booking.BookingStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingResponseDto {
    private Integer id;
    private BookingStatus status;
    private LocalDateTime bookingDate;
    private String address;
    private String serviceName;
    private List<String> staffName;
}
