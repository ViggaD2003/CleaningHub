package com.fpu.exe.cleaninghub.services.interfc;

import com.fpu.exe.cleaninghub.dto.request.CreateServiceRequestDto;
import com.fpu.exe.cleaninghub.dto.request.UpdateServiceRequestDto;
import com.fpu.exe.cleaninghub.dto.response.CreateServiceResponseDto;
import com.fpu.exe.cleaninghub.dto.response.ServiceDetailResponseDTO;
import com.fpu.exe.cleaninghub.dto.response.ServiceResponseDto;
import org.springframework.data.domain.Page;

public interface ServiceService {
    Page<ServiceResponseDto> getAvailableServices(String searchTerm, int pageIndex, int pageSize);
    CreateServiceResponseDto createService(CreateServiceRequestDto createServiceRequestDto);
    CreateServiceResponseDto updateService(Integer serviceId, UpdateServiceRequestDto updateServiceRequestDto);
    ServiceDetailResponseDTO getServiceDetailById(Integer id);
    void deleteService(Integer serviceId);
}
