package com.fpu.exe.cleaninghub.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;


@Configuration
public class SwaggerConfiguration {
    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("Cleaning-Hub-api")
                .packagesToScan("com.fpu.exe.cleaninghub.controller")
                .pathsToMatch("/api/**")
                .build();
    }

    @Bean
    public OpenAPI CleaningHubOpenAPI() {
        return new OpenAPI()
                .addServersItem(new Server().url("https://ch-api.arisavinh.dev"))
                .info(new Info().title("CLeaning Hub API")
                        .description("Cleaning hub application")
                        .version("v0.0.1")
                        .license(new License().name("Apache 2.0").url("http://springdoc.org")))
                .externalDocs(new ExternalDocumentation()
                        .description("Cleaning Hub Documentation")
                        .url("https://troll.vn"));
    }

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");

        source.registerCorsConfiguration("/v3/api-docs", config);
        return new CorsFilter(source);
    }
}