package com.fpu.exe.cleaninghub.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserProfileDTO {
    private Boolean gender;
    private Date dob;
    private String firstName;
    private String lastName;
    private String address;
    private String phoneNumber;
}
