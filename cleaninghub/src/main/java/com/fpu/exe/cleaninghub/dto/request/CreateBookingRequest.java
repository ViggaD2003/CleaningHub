package com.fpu.exe.cleaninghub.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateBookingRequest {
    private String email;
    private Integer serviceId;
    private Integer durationId;
    private String address;
    private Integer voucherId;
}
