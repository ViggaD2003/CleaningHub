package com.fpu.exe.cleaninghub.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fpu.exe.cleaninghub.entity.BookingDetail;
import com.fpu.exe.cleaninghub.entity.Duration;
import com.fpu.exe.cleaninghub.entity.Service;
import com.fpu.exe.cleaninghub.entity.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Builder
public class CreateBookingResponse {
    private Integer id;

    private String status = "Pending";

    @CreatedDate
    private LocalDate bookingDate;

    @LastModifiedDate
    private LocalDate updateDate;

    private String address;

    private BookingDetailResponse bookingDetail;

    private UserResponseDTO user;

    private UserResponseDTO staff;

    private ServiceResponseDto service;

    private DurationResponse duration;
}
