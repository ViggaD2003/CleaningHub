package com.fpu.exe.cleaninghub.services.impl;

import com.fpu.exe.cleaninghub.dto.response.ServiceResponse;
import com.fpu.exe.cleaninghub.repository.ServiceRepository;
import com.fpu.exe.cleaninghub.services.interfc.ServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ServiceServiceImpl implements ServiceService {
    @Autowired
    private ServiceRepository serviceRepository;
    @Override
    public Page<ServiceResponse> getAvailableServices(String searchTerm, int pageIndex, int pageSize) {
        Pageable pageable = PageRequest.of(pageIndex, pageSize);
        Page<com.fpu.exe.cleaninghub.entity.Service> servicePage = serviceRepository.GetAllService(searchTerm, pageable);
        return servicePage.map(service -> new ServiceResponse(
                service.getId(),
                service.getName(),
                service.getDescription(),
                service.getBasePrice(),
                service.getStatus()
        ));
    }
}
