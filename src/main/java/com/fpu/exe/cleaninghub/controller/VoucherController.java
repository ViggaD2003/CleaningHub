package com.fpu.exe.cleaninghub.controller;

import com.fpu.exe.cleaninghub.dto.request.VoucherRequest;
import com.fpu.exe.cleaninghub.dto.response.VoucherResponseDto;
import com.fpu.exe.cleaninghub.services.interfc.CategoryService;
import com.fpu.exe.cleaninghub.services.interfc.VoucherService;
import com.fpu.exe.cleaninghub.utils.Utility;
import com.fpu.exe.cleaninghub.utils.wapper.API;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
@RestController
@RequestMapping("/api/v1/vouchers")
@RequiredArgsConstructor
public class VoucherController {
    private final VoucherService voucherService;
    @PostMapping("create")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> createVoucher(VoucherRequest createVoucherRequest){
        try{
            voucherService.addVoucher(createVoucherRequest);
            return ResponseEntity.ok(API.Response.success("Create voucher successfully"));
        }catch (Exception ex){
            return ResponseEntity.ok(API.Response.error(HttpStatus.BAD_REQUEST,"Something went wrong", ex.getMessage()));
        }
    }


    @GetMapping
    public ResponseEntity<?> findAllVouchers(){
        try {
           List<VoucherResponseDto> list = voucherService.getAllVouchers();
           return ResponseEntity.ok(API.Response.success(list));
        } catch (Exception ex){
            return ResponseEntity.ok(API.Response.error(HttpStatus.BAD_REQUEST,"Something went wrong", ex.getMessage()));        }
    }
}
