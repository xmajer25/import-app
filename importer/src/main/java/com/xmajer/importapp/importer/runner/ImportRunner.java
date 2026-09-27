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
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
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
@ConditionalOnProperty(
        prefix = "import.runner",
        name = "enabled",
        havingValue = "true",
        matchIfMissing = true
)
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

        log.atInfo()
                .setMessage("Started address import")
                .log();

        Path archivePath = Files.createTempFile(
                "address-import-",
                ".zip"
        );

        try {
            downloader.download(
                    properties.sourceUrl(),
                    archivePath
            );

            ParsedAddressImport data = parseArchive(archivePath, options);

            log.atInfo()
                    .setMessage("Validating data")
                    .log();

            var violations = validator.validate(data);

            if (!violations.isEmpty()) {
                throw new ConstraintViolationException(violations);
            }

            log.atInfo()
                    .setMessage("Persisting data")
                    .log();

            ImportSaveSummary saveSummary = persistenceService.save(data);

            importJobService.recordSuccess(
                    startedAt,
                    properties.sourceUrl(),
                    saveSummary
            );

            log.atInfo()
                    .setMessage("Address import completed successfully")
                    .addKeyValue("duration", Duration.between(startedAt, Instant.now()).toMillis())
                    .log();

        } finally {
            deleteTempFile(archivePath);
        }
    }

    private ParsedAddressImport parseArchive(
            Path archivePath,
            ImportOptions options
    )
            throws IOException, XMLStreamException {

        try (
                ZipFile archive = new ZipFile(archivePath.toFile());
                InputStream xmlStream = archiveExtractor.openXmlStream(archive)
        ) {
            return parser.parse(xmlStream, options);
        }
    }

    private void deleteTempFile(Path path) {
        if (path == null) {
            return;
        }

        try {
            Files.deleteIfExists(path);
        } catch (IOException exception) {
            log.atWarn()
                    .setMessage("Could not delete temporary file")
                    .addKeyValue("path", path.toAbsolutePath().toString())
                    .setCause(exception)
                    .log();
        }
    }
}
