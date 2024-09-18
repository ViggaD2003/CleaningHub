package com.fpu.exe.cleaninghub.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ChangeForgotPasswordRequest {
    private String newPassword;
    private String confirmNewPassword;
}
