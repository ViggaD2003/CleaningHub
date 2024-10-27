package com.fpu.exe.cleaninghub.dto.response;

import lombok.Data;

@Data
public class AccountResponseDto {
    private Integer id;
    private String email;
    private String fullName;
    private String roleName;
    private Boolean status;
}
