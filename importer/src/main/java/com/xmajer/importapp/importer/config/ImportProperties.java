package com.xmajer.importapp.importer.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.net.URI;
import java.time.Duration;

@ConfigurationProperties(prefix = "import")
public record ImportProperties(
        URI sourceUrl,
        Duration requestTimeout
) {
}