package com.fpu.exe.cleaninghub.services.interfc;

import reactor.core.publisher.Mono;

public interface MapBoxService {

    Mono<String> calculateDistanceBetweenTwoLocation(Double lat1, Double lng1, Double lat2, Double lng2);
}
