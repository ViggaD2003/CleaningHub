package com.fpu.exe.cleaninghub.dto.response;

import com.fpu.exe.cleaninghub.entity.*;
import com.fpu.exe.cleaninghub.enums.Booking.BookingStatus;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class BookingDetailStaffResponse {
    private Integer id;

    private BookingStatus status;

    private String address;

    private UserResponseDTO user;
    private UserResponseDTO staff;

    private BookingDetailResponseDto bookingDetailResponseDto;

    private ServiceDetailResponseDTO service;


    private DurationResponseDTO duration;

    private RatingDTO rating;

    private LocalDateTime startDate;

    private LocalDateTime endDate;
}
