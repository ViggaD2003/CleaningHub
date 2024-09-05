package com.fpu.exe.cleaninghub.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
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
