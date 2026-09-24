package com.xmajer.importapp.importer.runner;

import com.xmajer.importapp.importer.archive.ArchiveExtractor;
import com.xmajer.importapp.importer.archive.ArchiveDownloader;
import com.xmajer.importapp.importer.config.ImportProperties;
import com.xmajer.importapp.importer.model.source.ParsedAddressImport;
import com.xmajer.importapp.importer.model.summary.ImportSaveSummary;
import com.xmajer.importapp.importer.parser.AddressXmlParser;
import com.xmajer.importapp.importer.service.ImportJobService;
import com.xmajer.importapp.importer.service.ImportPersistenceService;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.Instant;

@Slf4j
@Component
@RequiredArgsConstructor
public class ImportRunner implements ApplicationRunner {

    private final ImportProperties properties;
    private final ArchiveDownloader downloader;
    private final ArchiveExtractor archiveExtractor;
    private final AddressXmlParser parser;
    private final ImportPersistenceService persistenceService;
    private final ImportJobService importJobService;

    private final Validator validator;

    @Override
    public void run(ApplicationArguments args)
            throws IOException, InterruptedException, XMLStreamException {

        Instant startedAt = Instant.now();
        Path archivePath = null;

        try {
            log.info("Starting address import");

            archivePath = Files.createTempFile(
                    "address-import-",
                    ".zip"
            );

            log.info(
                    "Downloading archive from {}",
                    properties.sourceUrl()
            );

            downloader.download(
                    properties.sourceUrl(),
                    archivePath
            );

            InputStream xmlStream = archiveExtractor.openXmlStream(archivePath);

            ParsedAddressImport data = parser.parse(xmlStream);

            var violations = validator.validate(data);
            if (!violations.isEmpty()) {throw new ConstraintViolationException(violations);}

            ImportSaveSummary saveSummary = persistenceService.save(data);

            importJobService.recordSuccess(
                    startedAt,
                    properties.sourceUrl(),
                    saveSummary
            );

            log.info(
                    "Address import completed in {} ms",
                    Duration.between(startedAt, Instant.now()).toMillis()
            );

        } catch (IOException
                 | InterruptedException
                 | RuntimeException
                 | XMLStreamException exception) {

            importJobService.recordFailure(
                    startedAt,
                    properties.sourceUrl(),
                    exception
            );

            throw exception;

        } finally {
            deleteTempFile(archivePath);
        }
    }

    private void deleteTempFile(Path path) {
        if (path == null) {
            return;
        }

        try {
            Files.deleteIfExists(path);
        } catch (IOException exception) {
            log.warn(
                    "Could not delete temporary archive {}",
                    path,
                    exception
            );
        }
    }
}
