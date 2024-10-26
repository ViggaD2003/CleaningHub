package com.fpu.exe.cleaninghub.dto.response;

import com.fpu.exe.cleaninghub.entity.*;
import com.fpu.exe.cleaninghub.enums.Booking.BookingStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BookingDetailStaffResponse {
    private Integer id;

    private BookingStatus status;

    private String address;

    private UserResponseDTO user;

    private BookingDetailResponseDto bookingDetailResponseDto;

    private Service service;


    private Duration duration;

    private Rating rating;

    private LocalDateTime startDate;

    private LocalDateTime endDate;
}
