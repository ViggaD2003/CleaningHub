package com.fpu.exe.cleaninghub.services.interfc;

import com.fpu.exe.cleaninghub.dto.response.ServiceResponse;
import org.springframework.data.domain.Page;

public interface ServiceService {
    Page<ServiceResponse> getAvailableServices(String searchTerm, int pageIndex, int pageSize);
}
