package com.ssafy.enjoytrip.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(info = @Info(title = "SSAFY TRIP API 명세서", version = "${api.version}"))
public class SwaggerConfig {
    @Value("${api.prefix}")
    private String apiPrefix;
    @Value("${api.version}")
    private String apiVersion;

    @Bean
    GroupedOpenApi userOpenApi() {
        String paths = "/%s/%s/users/**".formatted(apiPrefix, apiVersion);
        return GroupedOpenApi.builder()
                .group("User API")
                .pathsToMatch(paths)
                .build();
    }

    @Bean
    GroupedOpenApi authOpenApi() {
        String paths = "/%s/%s/auth/**".formatted(apiPrefix, apiVersion);
        return GroupedOpenApi.builder()
                .group("Auth API")
                .pathsToMatch(paths)
                .build();
    }

    @Bean
    GroupedOpenApi attractionOpenApi() {
        String paths = "/%s/%s/attractions/**".formatted(apiPrefix, apiVersion);
        return GroupedOpenApi.builder()
                .group("Attraction API")
                .pathsToMatch(paths)
                .build();
    }

    @Bean
    GroupedOpenApi planOpenApi() {
        String paths = "/%s/%s/plans/**".formatted(apiPrefix, apiVersion);
        return GroupedOpenApi.builder()
                .group("Plan API")
                .pathsToMatch(paths)
                .build();
    }

    @Bean
    GroupedOpenApi tripostOpenApi() {
        String paths = "/%s/%s/triposts/**".formatted(apiPrefix, apiVersion);
        return GroupedOpenApi.builder()
                .group("Post API")
                .pathsToMatch(paths)
                .build();
    }

    @Bean
    GroupedOpenApi feedOpenApi() {
        String paths = "/%s/%s/feeds/**".formatted(apiPrefix, apiVersion);
        return GroupedOpenApi.builder()
                .group("Feed API")
                .pathsToMatch(paths)
                .build();
    }
}
