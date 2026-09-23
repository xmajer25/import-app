package com.xmajer.importapp.importer.archive;

import com.xmajer.importapp.importer.model.ParsedAddressData;
import com.xmajer.importapp.importer.parser.AddressXmlParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Locale;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

@Slf4j
@Component
@RequiredArgsConstructor
public class AddressArchiveReader {

    private final AddressXmlParser parser;

    public ParsedAddressData read(Path archivePath)
            throws IOException, XMLStreamException {

        try (ZipFile archive = new ZipFile(archivePath.toFile())) {

            ZipEntry xmlEntry = findXmlEntry(archive);

            log.info(
                    "Parsing XML entry {}",
                    xmlEntry.getName()
            );

            try (var xmlStream = archive.getInputStream(xmlEntry)) {
                return parser.parse(xmlStream);
            }
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
}