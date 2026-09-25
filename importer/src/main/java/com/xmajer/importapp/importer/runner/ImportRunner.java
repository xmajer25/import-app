package com.xmajer.importapp.importer.runner;

import com.xmajer.importapp.importer.archive.ArchiveExtractor;
import com.xmajer.importapp.importer.archive.ArchiveDownloader;
import com.xmajer.importapp.importer.config.ImportProperties;
import com.xmajer.importapp.importer.cli.ImportOptions;
import com.xmajer.importapp.importer.cli.ImportOptionsParser;
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
import java.util.zip.ZipFile;

@Slf4j
@Component
@RequiredArgsConstructor
public class ImportRunner implements ApplicationRunner {

    private final ImportProperties properties;
    private final ArchiveDownloader downloader;
    private final ArchiveExtractor archiveExtractor;
    private final AddressXmlParser parser;
    private final ImportOptionsParser optionsParser;
    private final ImportPersistenceService persistenceService;
    private final ImportJobService importJobService;

    private final Validator validator;

    @Override
    public void run(ApplicationArguments args)
            throws IOException, InterruptedException, XMLStreamException {

        Instant startedAt = Instant.now();
        ImportOptions options = optionsParser.parse(args);

        try {
            executeImport(startedAt, options);
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
        }
    }

    private void executeImport(
            Instant startedAt,
            ImportOptions options
    )
            throws IOException, InterruptedException, XMLStreamException {

        log.info("Starting address import");

        Path archivePath = Files.createTempFile(
                "address-import-",
                ".zip"
        );

        try {
            downloader.download(
                    properties.sourceUrl(),
                    archivePath
            );

            ParsedAddressImport data = parseArchive(archivePath);

            var violations = validator.validate(data);

            if (!violations.isEmpty()) {
                throw new ConstraintViolationException(violations);
            }

            ImportSaveSummary saveSummary =
                    persistenceService.save(data);

            importJobService.recordSuccess(
                    startedAt,
                    properties.sourceUrl(),
                    saveSummary
            );

            log.info(
                    "Address import completed in {} ms",
                    Duration.between(startedAt, Instant.now()).toMillis()
            );

        } finally {
            deleteTempFile(archivePath);
        }
    }

    private ParsedAddressImport parseArchive(Path archivePath)
            throws IOException, XMLStreamException {

        try (
                ZipFile archive = new ZipFile(archivePath.toFile());
                InputStream xmlStream = archiveExtractor.openXmlStream(archive)
        ) {
            return parser.parse(xmlStream);
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
