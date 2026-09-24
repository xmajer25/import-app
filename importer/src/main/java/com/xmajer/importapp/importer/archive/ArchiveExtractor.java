package com.xmajer.importapp.importer.archive;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.Locale;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

@Slf4j
@Component
public class ArchiveExtractor {

    public InputStream openXmlStream(Path archivePath)
            throws IOException {

        ZipFile archive = new ZipFile(archivePath.toFile());

        try {
            ZipEntry xmlEntry = findXmlEntry(archive);

            log.info(
                    "Extracting XML entry {}",
                    xmlEntry.getName()
            );

            return closeArchiveWithStream(
                    archive.getInputStream(xmlEntry),
                    archive
            );
        } catch (IOException exception) {
            archive.close();
            throw exception;
        }
    }

    private ZipEntry findXmlEntry(ZipFile archive)
            throws IOException {

        var xmlEntries = archive.stream()
                .filter(entry -> !entry.isDirectory())
                .filter(this::isXml)
                .toList();

        if (xmlEntries.size() != 1) {
            throw new IOException(
                    "Expected exactly one XML entry, found "
                            + xmlEntries.size()
            );
        }

        return xmlEntries.getFirst();
    }

    private boolean isXml(ZipEntry entry) {
        return entry.getName()
                .toLowerCase(Locale.ROOT)
                .endsWith(".xml");
    }

    private InputStream closeArchiveWithStream(
            InputStream stream,
            ZipFile archive
    ) {
        return new FilterInputStream(stream) {
            @Override
            public void close() throws IOException {
                try {
                    super.close();
                } finally {
                    archive.close();
                }
            }
        };
    }
}
