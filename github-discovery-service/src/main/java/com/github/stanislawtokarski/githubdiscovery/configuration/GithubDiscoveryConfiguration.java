package com.github.stanislawtokarski.githubdiscovery.configuration;

import com.github.stanislawtokarski.githubdiscovery.service.RestTemplateExceptionHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Configuration
public class GithubDiscoveryConfiguration {

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder,
                                     RestTemplateExceptionHandler errorHandler,
                                     @Value("${github.api.url}") String githubApiUrl) {
        return restTemplateBuilder
                .rootUri(githubApiUrl)
                .errorHandler(errorHandler)
                .setConnectTimeout(Duration.ofMillis(1000))
                .build();
    }
}
