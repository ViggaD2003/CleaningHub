package com.fpu.exe.cleaninghub.controller;

import com.fpu.exe.cleaninghub.dto.request.CreateServiceRequestDto;
import com.fpu.exe.cleaninghub.dto.request.UpdateServiceRequestDto;
import com.fpu.exe.cleaninghub.dto.response.CreateServiceResponseDto;
import com.fpu.exe.cleaninghub.dto.response.ServiceResponseDto;
import com.fpu.exe.cleaninghub.services.interfc.ServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/services")
public class ServiceController {
    @Autowired
    private ServiceService serviceService;
    @GetMapping("/available")
    public ResponseEntity<Page<ServiceResponseDto>> getAllServices(@RequestParam(defaultValue = "") String searchTerm,
                                                                   @RequestParam(defaultValue = "0") int pageIndex,
                                                                   @RequestParam(defaultValue = "10") int pageSize) {
        Page<ServiceResponseDto> services = serviceService.getAvailableServices(searchTerm, pageIndex, pageSize);
        return ResponseEntity.ok(services);
    }
    @PostMapping
    public ResponseEntity<CreateServiceResponseDto> createService (@RequestBody CreateServiceRequestDto serviceRequestDto){
        CreateServiceResponseDto response = serviceService.createService(serviceRequestDto);
        return ResponseEntity.ok(response);
    }
    @PutMapping("{id}")
    public ResponseEntity<CreateServiceResponseDto> updateService(
            @PathVariable Integer id,
            @RequestBody UpdateServiceRequestDto updateServiceRequestDto) {
        CreateServiceResponseDto updatedService = serviceService.updateService(id, updateServiceRequestDto);
        return ResponseEntity.ok(updatedService);
    }
    @GetMapping("{id}")
    public ResponseEntity<?> getDetailService(@PathVariable Integer id){
        return ResponseEntity.ok(serviceService.getServiceDetailById(id));
    }
    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteService(@PathVariable Integer id) {
        serviceService.deleteService(id);
        return ResponseEntity.noContent().build();
    }
}
