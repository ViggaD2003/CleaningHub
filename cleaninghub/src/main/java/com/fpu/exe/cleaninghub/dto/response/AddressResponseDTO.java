package com.fpu.exe.cleaninghub.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AddressResponseDTO {
    private String street;
    private String city;
    private String state;
    private String zipCode;
    private String country;
}