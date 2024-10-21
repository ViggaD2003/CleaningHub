package com.fpu.exe.cleaninghub.controller;

import com.fpu.exe.cleaninghub.dto.request.CreateServiceRequestDto;
import com.fpu.exe.cleaninghub.dto.request.UpdateServiceRequestDto;
import com.fpu.exe.cleaninghub.dto.response.CreateServiceResponseDto;
import com.fpu.exe.cleaninghub.dto.response.ServiceResponseDto;
import com.fpu.exe.cleaninghub.services.interfc.ServiceService;
import com.fpu.exe.cleaninghub.utils.wapper.API;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/services")
public class ServiceController {

    @Autowired
    private ServiceService serviceService;

    @GetMapping("/available")
    public ResponseEntity<?> getAllServices(@RequestParam(defaultValue = "") String searchTerm,
                                            @RequestParam(defaultValue = "0") int pageIndex,
                                            @RequestParam(defaultValue = "10") int pageSize) {
        Page<ServiceResponseDto> services = serviceService.getAvailableServices(searchTerm, pageIndex, pageSize);
        return ResponseEntity.ok(API.Response.success(services));
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> createService(@Valid @RequestBody CreateServiceRequestDto serviceRequestDto) {
        try {
            CreateServiceResponseDto response = serviceService.createService(serviceRequestDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(API.Response.success(response));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(API.Response.error(HttpStatus.BAD_REQUEST, "Something went wrong", e.getMessage()));
        }
    }

    @PutMapping("{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> updateService(@PathVariable Integer id,
                                           @Valid @RequestBody UpdateServiceRequestDto updateServiceRequestDto) {
        try {
            CreateServiceResponseDto updatedService = serviceService.updateService(id, updateServiceRequestDto);
            return ResponseEntity.ok(API.Response.success(updatedService));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(API.Response.error(HttpStatus.BAD_REQUEST, "Something went wrong", e.getMessage()));
        }
    }

    @GetMapping("{id}")
    public ResponseEntity<?> getDetailService(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok(API.Response.success(serviceService.getServiceDetailById(id)));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(API.Response.error(HttpStatus.BAD_REQUEST, "Something went wrong!!", e.getMessage()));
        }
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> deleteService(@PathVariable Integer id) {
        try {
            serviceService.deleteService(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(API.Response.error(HttpStatus.BAD_REQUEST, "Something went wrong!!", e.getMessage()));
        }
    }
}

