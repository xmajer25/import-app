package com.xmajer.importapp.importer.service;

import com.xmajer.importapp.importer.archive.AddressArchiveReader;
import com.xmajer.importapp.importer.config.ImportProperties;
import com.xmajer.importapp.importer.download.ArchiveDownloader;
import com.xmajer.importapp.importer.validation.ImportDataValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Slf4j
@Service
@RequiredArgsConstructor
public class AddressImportService {

    private final ImportProperties properties;
    private final ArchiveDownloader downloader;
    private final AddressArchiveReader archiveReader;
    private final ImportDataValidator validator;
    private final ImportPersistenceService persistenceService;

    public void importData()
            throws IOException, InterruptedException, XMLStreamException {

        Path archivePath = Files.createTempFile(
                "address-import-",
                ".zip"
        );

        try {
            log.info(
                    "Downloading archive from {}",
                    properties.sourceUrl()
            );

            downloader.download(
                    properties.sourceUrl(),
                    archivePath
            );

            var data = archiveReader.read(archivePath);

            validator.validate(data);

            persistenceService.save(data);

        } finally {
            deleteTempFile(archivePath);
        }
    }

    private void deleteTempFile(Path path) {
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