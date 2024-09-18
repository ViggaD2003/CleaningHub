package com.fpu.exe.cleaninghub.dto.request;

import com.fpu.exe.cleaninghub.enums.Payment.PaymentMethod;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
public class CreateBookingRequestDTO {
    @NotNull(message = "Email cannot be null")
    @Email(message = "Invalid email format")
    private String email;

    @NotNull(message = "Service ID cannot be null")
    private Integer serviceId;

    @NotNull(message = "Duration ID cannot be null")
    private Integer durationId;

    @NotNull(message = "Address cannot be null")
    private AddressRequestDTO address;  // Make sure this class is also properly validated

    private Integer voucherId;  // Optional, hence no validation required

    @NotNull(message = "Payment method cannot be null")
    private PaymentMethod paymentMethod;  // Add payment method
}
