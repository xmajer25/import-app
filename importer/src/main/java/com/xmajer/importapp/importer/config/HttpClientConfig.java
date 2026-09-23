package com.xmajer.importapp.importer.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.http.HttpClient;
import java.time.Duration;

@Configuration(proxyBeanMethods = false)
public class HttpClientConfig {

    @Bean(destroyMethod = "close")
    public HttpClient httpClient(
            @Value("${import.connect-timeout}") Duration connectTimeout
    ) {
        return HttpClient.newBuilder()
                .connectTimeout(connectTimeout)
                .followRedirects(HttpClient.Redirect.NORMAL)
                .build();
    }
}