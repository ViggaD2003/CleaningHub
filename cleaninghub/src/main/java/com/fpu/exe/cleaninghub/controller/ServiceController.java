package com.fpu.exe.cleaninghub.controller;

import com.fpu.exe.cleaninghub.dto.request.CreateServiceRequestDto;
import com.fpu.exe.cleaninghub.dto.request.UpdateServiceRequestDto;
import com.fpu.exe.cleaninghub.dto.response.CreateServiceResponseDto;
import com.fpu.exe.cleaninghub.dto.response.ServiceResponseDto;
import com.fpu.exe.cleaninghub.services.interfc.ServiceService;
import com.fpu.exe.cleaninghub.utils.wapper.API;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<?> createService (@RequestBody CreateServiceRequestDto serviceRequestDto){
        try{
            CreateServiceResponseDto response = serviceService.createService(serviceRequestDto);
            return ResponseEntity.ok(response);
        }catch (Exception e){
            return ResponseEntity.ok(API.Response.error(HttpStatus.BAD_REQUEST, "Something went wrong", e.getMessage()));
        }
    }
    @PutMapping("{id}")
    public ResponseEntity<?> updateService(
            @PathVariable Integer id,
            @RequestBody UpdateServiceRequestDto updateServiceRequestDto) {
        try{
            CreateServiceResponseDto updatedService = serviceService.updateService(id, updateServiceRequestDto);
            return ResponseEntity.ok(updatedService);
        }catch (Exception e){
            return ResponseEntity.ok(API.Response.error(HttpStatus.BAD_REQUEST, "Something went wrong", e.getMessage()));
        }
    }
    @GetMapping("{id}")
    public ResponseEntity<?> getDetailService(@PathVariable Integer id){
        try{
            return ResponseEntity.ok(serviceService.getServiceDetailById(id));
        }catch (Exception e){
            return ResponseEntity.ok(API.Response.error(HttpStatus.BAD_REQUEST, "Something went wrong!!", e.getMessage()));
        }
    }
    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteService(@PathVariable Integer id) {
        serviceService.deleteService(id);
        return ResponseEntity.noContent().build();
    }
}
