package com.fpu.exe.cleaninghub.services.interfc;

import com.fpu.exe.cleaninghub.dto.request.VoucherRequest;
import com.fpu.exe.cleaninghub.dto.response.VoucherResponseDto;

import java.util.List;
public interface VoucherService {
    void addVoucher(VoucherRequest createVoucherRequest);

    List<VoucherResponseDto> getAllVouchers();
}
