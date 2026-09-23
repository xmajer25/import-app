package com.xmajer.importapp.importer.downloader;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Path;
import java.time.Duration;

@Component
public class ArchiveDownloader {

    private final HttpClient httpClient;
    private final Duration requestTimeout;

    public ArchiveDownloader(
            HttpClient httpClient,
            @Value("${import.request-timeout}") Duration requestTimeout
    ) {
        this.httpClient = httpClient;
        this.requestTimeout = requestTimeout;
    }

    public void download(URI source, Path destination)
            throws IOException, InterruptedException {

        HttpRequest request = HttpRequest.newBuilder(source)
                .timeout(requestTimeout)
                .GET()
                .build();

        HttpResponse<Path> response;

        try {
            response = httpClient.send(
                    request,
                    HttpResponse.BodyHandlers.ofFile(destination)
            );
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            throw exception;
        }

        if (response.statusCode() != 200) {
            throw new IOException(
                    "Archive download failed: HTTP " + response.statusCode()
            );
        }
    }
}