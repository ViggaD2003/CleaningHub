package com.fpu.exe.cleaninghub.dto.request;

import com.fpu.exe.cleaninghub.enums.Payment.PaymentMethod;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
public class CreateBookingRequestDTO {

    @NotNull(message = "Service ID cannot be null")
    private Integer serviceId;

    @NotNull(message = "Duration ID cannot be null")
    private Integer durationId;

    @NotNull(message = "Number of worker cannot be null")
    @Min(value = 1, message = "Number of worker must be at least 1")
    @Max(value = 5, message = "Number of worker must not be greater than 5")
    private Integer numberOfWorker;

    @NotNull(message = "Address cannot be null")
    private String address;

    @NotNull(message = "Latitude cannot be null")
    private Double latitude;

    @NotNull(message = "Longitude cannot be null")
    private Double longitude;

    private Integer voucherId;  // Optional, hence no validation required

    @NotNull(message = "Payment method cannot be null")
    private PaymentMethod paymentMethod;  // Add payment method

    private LocalDateTime startTime;
}
