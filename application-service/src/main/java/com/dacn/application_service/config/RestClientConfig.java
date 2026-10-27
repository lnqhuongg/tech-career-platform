package com.dacn.application_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {
    @Bean
    public RestClient restClient() {
        return RestClient.create();
    }

    @Bean("userRestClient")
    public RestClient userRestClient(RestClient.Builder builder) {
        return builder
                .baseUrl("http://localhost:8082")
                .build();
    }

    @Bean("jobRestClient")
    public RestClient jobRestClient(RestClient.Builder builder) {
        return builder
                .baseUrl("http://localhost:8083")
                .build();
    }

    @Bean("resumeRestClient")
    public RestClient resumeRestClient(RestClient.Builder builder) {
        return builder
                .baseUrl("http://localhost:8085")
                .build();
    }
}
