package com.fpu.exe.cleaninghub.services.impl;

import com.fpu.exe.cleaninghub.services.interfc.MapBoxService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class MapBoxServiceImpl implements MapBoxService {

    @Value("${mapbox.api.key}")

    private String apiKey;

    @Value("${mapbox.api.url}")
    private  String apiUrl;

    private final WebClient webClient;

    public MapBoxServiceImpl(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }

    @Override
    public Mono<String> calculateDistanceBetweenTwoLocation(Double lat1, Double lng1, Double lat2, Double lng2) {
        String urlTemplate = "%s/%f,%f;%f,%f?alternatives=true&geometries=geojson&language=en&overview=full&steps=true&access_token=%s";
        String url = String.format(urlTemplate, apiUrl, lng1, lat1, lng2, lat2, apiKey);

        return this.webClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(String.class);
    }
}
