package com.fpu.exe.cleaninghub.services.impl;

import com.fpu.exe.cleaninghub.entity.Duration;
import com.fpu.exe.cleaninghub.repository.DurationRepository;
import com.fpu.exe.cleaninghub.services.interfc.DurationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DurationServiceImpl implements DurationService {
    @Autowired
    private DurationRepository durationRepository;
    @Override
    public List<Duration> getDurationsByServiceId(int serviceId) {
        return durationRepository.findByServiceId(serviceId);
    }
}
