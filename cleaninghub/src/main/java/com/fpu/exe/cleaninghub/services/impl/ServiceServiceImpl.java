package com.fpu.exe.cleaninghub.services.impl;

import com.fpu.exe.cleaninghub.dto.request.CreateServiceRequestDto;
import com.fpu.exe.cleaninghub.dto.request.UpdateServiceRequestDto;
import com.fpu.exe.cleaninghub.dto.response.CreateServiceResponseDto;
import com.fpu.exe.cleaninghub.dto.response.ServiceResponseDto;
import com.fpu.exe.cleaninghub.entity.Category;
import com.fpu.exe.cleaninghub.repository.CategoryRepository;
import com.fpu.exe.cleaninghub.repository.ServiceRepository;
import com.fpu.exe.cleaninghub.services.interfc.ServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.auditing.CurrentDateTimeProvider;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class ServiceServiceImpl implements ServiceService {
    @Autowired
    private ServiceRepository serviceRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Override
    public Page<ServiceResponseDto> getAvailableServices(String searchTerm, int pageIndex, int pageSize) {
        Pageable pageable = PageRequest.of(pageIndex, pageSize);
        Page<com.fpu.exe.cleaninghub.entity.Service> servicePage = serviceRepository.GetAllService(searchTerm, pageable);
        return servicePage.map(service -> new ServiceResponseDto(
                service.getId(),
                service.getName(),
                service.getDescription(),
                service.getBasePrice(),
                service.getStatus()
        ));
    }

    @Override
    public CreateServiceResponseDto createService(CreateServiceRequestDto createServiceRequestDto) {
        Category category = categoryRepository.findById(Long.valueOf(createServiceRequestDto.getCategoryId())).orElseThrow(() -> new RuntimeException("Category not found"));
        com.fpu.exe.cleaninghub.entity.Service service = com.fpu.exe.cleaninghub.entity.Service.builder()
                .name(createServiceRequestDto.getName())
                .description(createServiceRequestDto.getDescription())
                .basePrice(createServiceRequestDto.getBasePrice())
                .status(createServiceRequestDto.getStatus())
                .createDate(LocalDate.now())
                .category(category)
                .build();
        com.fpu.exe.cleaninghub.entity.Service savedService = serviceRepository.save(service);
        CreateServiceResponseDto responseDto = new CreateServiceResponseDto();
        responseDto.setId(savedService.getId());
        responseDto.setName(savedService.getName());
        responseDto.setDescription(savedService.getDescription());
        responseDto.setBasePrice(savedService.getBasePrice());
        responseDto.setStatus(savedService.getStatus());
        responseDto.setCategoryName(savedService.getCategory().getName());
        return responseDto;
    }

    @Override
    public CreateServiceResponseDto updateService(Integer serviceId, UpdateServiceRequestDto updateServiceRequestDto) {
        com.fpu.exe.cleaninghub.entity.Service existingService = serviceRepository.findById(serviceId)
                .orElseThrow(() -> new RuntimeException("Service not found"));
        Category category = categoryRepository.findById(Long.valueOf(updateServiceRequestDto.getCategoryId()))
                .orElseThrow(() -> new RuntimeException("Category not found"));

        existingService.setName(updateServiceRequestDto.getName());
        existingService.setDescription(updateServiceRequestDto.getDescription());
        existingService.setBasePrice(updateServiceRequestDto.getBasePrice());
        existingService.setStatus(updateServiceRequestDto.getStatus());
        existingService.setCategory(category);

        com.fpu.exe.cleaninghub.entity.Service updatedService = serviceRepository.save(existingService);

        CreateServiceResponseDto responseDto = new CreateServiceResponseDto();
        responseDto.setId(updatedService.getId());
        responseDto.setName(updatedService.getName());
        responseDto.setDescription(updatedService.getDescription());
        responseDto.setBasePrice(updatedService.getBasePrice());
        responseDto.setStatus(updatedService.getStatus());
        responseDto.setCategoryName(updatedService.getCategory().getName());
        return responseDto;
    }

    @Override
    public void deleteService(Integer serviceId) {
        com.fpu.exe.cleaninghub.entity.Service existingService = serviceRepository.findById(serviceId)
                .orElseThrow(() -> new RuntimeException("Service not found"));
        existingService.setStatus("Deleted");
        serviceRepository.save(existingService);
    }
}
