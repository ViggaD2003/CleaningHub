package com.fpu.exe.cleaninghub.dto.response;

import com.fpu.exe.cleaninghub.enums.Booking.BookingStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class CreateBookingResponseDTO {
    private Integer id;
    private BookingStatus status;
    private BookingDetailResponseDto bookingDetail;
    private String address;
    private Double latitude;
    private Double longitude;
    private UserResponseDTO user;
    private List<UserResponseDTO> staff;
    private ServiceDetailResponseDTO service;
    private DurationResponseDTO duration;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    private LocalDateTime startedAt;
    private LocalDateTime endAt;
}
