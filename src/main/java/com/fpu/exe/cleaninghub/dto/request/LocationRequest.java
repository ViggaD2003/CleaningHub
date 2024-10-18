package com.fpu.exe.cleaninghub.dto.request;

import groovy.transform.Sealed;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@Sealed
public class LocationRequest {
    private Double latitude;
    private Double longitude;
}
