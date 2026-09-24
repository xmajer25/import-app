package com.xmajer.importapp.importer.archive;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

@Slf4j
@Component
public class ArchiveExtractor {

    public InputStream openXmlStream(ZipFile archive)
            throws IOException {

        ZipEntry xmlEntry = findXmlEntry(archive);

        log.info(
                "Extracting XML entry {}",
                xmlEntry.getName()
        );

        return archive.getInputStream(xmlEntry);
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
}
