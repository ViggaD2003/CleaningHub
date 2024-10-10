package com.fpu.exe.cleaninghub.services.interfc;

import com.fpu.exe.cleaninghub.dto.request.DurationRequest;
import com.fpu.exe.cleaninghub.dto.request.VoucherRequest;
import com.fpu.exe.cleaninghub.entity.Duration;

import java.util.List;

public interface DurationService {
    void createDuration(DurationRequest createDurationRequest);
    void updateDuration(Integer id, DurationRequest updateDurationRequest);
    List<Duration> getDurationsByServiceId(int serviceId);
}
