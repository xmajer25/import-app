package com.xmajer.importapp.importer.archive;

import com.xmajer.importapp.importer.config.ImportProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Path;

@Slf4j
@Component
public class ArchiveDownloader {

    private final HttpClient httpClient;
    private final ImportProperties properties;

    public ArchiveDownloader(
            HttpClient httpClient,
            ImportProperties properties
    ) {
        this.httpClient = httpClient;
        this.properties = properties;
    }

    public void download(URI source, Path destination)
            throws IOException, InterruptedException {

        HttpRequest request = HttpRequest.newBuilder(source)
                .timeout(properties.requestTimeout())
                .GET()
                .build();

        HttpResponse<Path> response;

        log.atInfo()
                .setMessage("Sending HTTP download request")
                .addKeyValue("source", source.toString())
                .addKeyValue("destination", destination.toString())
                .log();


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
                    "Archive download failed: HTTP "
                            + response.statusCode()
            );
        }
    }
}
