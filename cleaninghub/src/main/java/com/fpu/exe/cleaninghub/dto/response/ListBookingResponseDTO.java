package com.fpu.exe.cleaninghub.dto.response;

import com.fpu.exe.cleaninghub.entity.*;
import com.fpu.exe.cleaninghub.enums.Booking.BookingStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ListBookingResponseDTO {
    private Integer id;

    private BookingStatus status;

    private Address address;


    private User user;

    private User staff;


    private Service service;


    private Duration duration;

    private Rating rating;

    private LocalDateTime startDate;

    private LocalDateTime endDate;
}
