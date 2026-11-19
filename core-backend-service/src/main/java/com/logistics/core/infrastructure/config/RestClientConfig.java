package com.logistics.core.infrastructure.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {
    private final String aiAgentBaseUrl;
   
    public RestClientConfig(@Value("${ai-agent.base-url}") String aiAgentBaseUrl) {
        this.aiAgentBaseUrl = aiAgentBaseUrl;
    }
    @Bean
    public RestClient aiAgentRestClient(RestClient.Builder builder) {
        return builder
                .baseUrl(aiAgentBaseUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }
}