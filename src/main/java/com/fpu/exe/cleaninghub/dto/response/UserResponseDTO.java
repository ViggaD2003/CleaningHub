package com.fpu.exe.cleaninghub.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDTO {
    private Integer id;
    private String email;
    private Boolean status;
    private Boolean gender;
    private Date dob;
    private String firstName;
    private String lastName;
    private String address;
    private String phoneNumber;
}
