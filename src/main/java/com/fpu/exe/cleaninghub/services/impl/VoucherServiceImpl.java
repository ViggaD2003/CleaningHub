package com.fpu.exe.cleaninghub.services.impl;

import com.fpu.exe.cleaninghub.dto.request.VoucherRequest;
import com.fpu.exe.cleaninghub.entity.Voucher;
import com.fpu.exe.cleaninghub.repository.CategoryRepository;
import com.fpu.exe.cleaninghub.repository.VoucherRepository;
import com.fpu.exe.cleaninghub.services.interfc.VoucherService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class VoucherServiceImpl implements VoucherService {
    @Autowired
    private VoucherRepository voucherRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Override
    public void addVoucher(VoucherRequest createVoucherRequest) {
        if (createVoucherRequest.getExpiredDate().isBefore(LocalDateTime.now())){
            throw new IllegalArgumentException("Expire date must be after now");

        }
        voucherRepository.save(modelMapper.map(createVoucherRequest, Voucher.class));
    }
}
