package com.xmajer.importapp.importer.runner;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;

import com.xmajer.importapp.importer.service.AddressImportService;
import org.springframework.boot.ApplicationArguments;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;

@Slf4j
@Component
@RequiredArgsConstructor
public class ImportRunner implements ApplicationRunner {

    private final AddressImportService importService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Instant startedAt = Instant.now();

        log.info("Starting address import");

        importService.importData();

        log.info(
                "Address import completed in {} ms",
                Duration.between(startedAt, Instant.now()).toMillis()
        );
    }
}