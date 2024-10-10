package com.fpu.exe.cleaninghub.services.impl;

import com.fpu.exe.cleaninghub.dto.request.DurationRequest;
import com.fpu.exe.cleaninghub.entity.Duration;
import com.fpu.exe.cleaninghub.repository.DurationRepository;
import com.fpu.exe.cleaninghub.repository.ServiceRepository;
import com.fpu.exe.cleaninghub.services.interfc.DurationService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DurationServiceImpl implements DurationService {
    private final DurationRepository durationRepository;
    private final ServiceRepository serviceRepository;
    private final ModelMapper modelMapper;
    @Override
    public void createDuration(DurationRequest createDurationRequest) {
        com.fpu.exe.cleaninghub.entity.Service service = serviceRepository.findById(createDurationRequest.getServiceId())
                .orElseThrow(() -> new IllegalArgumentException("Service not found"));
        Duration duration = Duration.builder()
                .service(service)
                .durationInHours(createDurationRequest.getDurationInHours())
                .area(createDurationRequest.getArea())
                .price(createDurationRequest.getPrice())
                .build();
        durationRepository.save(duration);
    }

    @Override
    public void updateDuration(Integer id, DurationRequest updateDurationRequest) {
        Duration duration = durationRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Duration not found"));
        com.fpu.exe.cleaninghub.entity.Service service = serviceRepository.findById(updateDurationRequest.getServiceId())
                .orElseThrow(() -> new IllegalArgumentException("Service not found"));
        modelMapper.map(updateDurationRequest, duration);
        duration.setService(service);
        durationRepository.save(duration);

    }

    @Override
    public List<Duration> getDurationsByServiceId(int serviceId) {
        return durationRepository.findByServiceId(serviceId);
    }
}
