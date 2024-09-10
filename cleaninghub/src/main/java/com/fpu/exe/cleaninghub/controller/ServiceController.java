package com.fpu.exe.cleaninghub.controller;

import com.fpu.exe.cleaninghub.dto.response.ServiceResponse;
import com.fpu.exe.cleaninghub.services.interfc.ServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/services")
public class ServiceController {
    @Autowired
    private ServiceService serviceService;
    @GetMapping("/available")
    public ResponseEntity<Page<ServiceResponse>> getAllServices(@RequestParam(defaultValue = "") String searchTerm,
                                                                @RequestParam(defaultValue = "0") int pageIndex,
                                                                @RequestParam(defaultValue = "10") int pageSize) {
        Page<ServiceResponse> services = serviceService.getAvailableServices(searchTerm, pageIndex, pageSize);
        return ResponseEntity.ok(services);
    }
}
