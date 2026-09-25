package com.xmajer.importapp.importer.parser.support;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import static javax.xml.stream.XMLStreamConstants.END_ELEMENT;
import static javax.xml.stream.XMLStreamConstants.START_ELEMENT;

public final class StaxReaderUtils {

    private StaxReaderUtils() {
    }

    public static String readText(XMLStreamReader reader)
            throws XMLStreamException {

        return reader.getElementText().strip();
    }

    public static Integer readInteger(XMLStreamReader reader)
            throws XMLStreamException {

        String value = readText(reader);

        try {
            return Integer.valueOf(value);
        } catch (NumberFormatException e) {
            throw new XMLStreamException(
                    "Invalid integer value: " + value,
                    e
            );
        }
    }

    public static Long readLong(XMLStreamReader reader)
            throws XMLStreamException {

        String value = readText(reader);

        try {
            return Long.valueOf(value);
        } catch (NumberFormatException e) {
            throw new XMLStreamException(
                    "Invalid long value: " + value,
                    e
            );
        }
    }

    public static Instant readInstantUtc(XMLStreamReader reader)
            throws XMLStreamException {

        String value = readText(reader);

        try {
            return LocalDateTime.parse(value)
                    .toInstant(ZoneOffset.UTC);
        } catch (RuntimeException e) {
            throw new XMLStreamException(
                    "Invalid date-time value: " + value,
                    e
            );
        }
    }

    public static void skipElement(XMLStreamReader reader)
            throws XMLStreamException {

        int depth = 1;

        while (depth > 0 && reader.hasNext()) {
            int event = reader.next();

            if (event == START_ELEMENT) {
                depth++;
            } else if (event == END_ELEMENT) {
                depth--;
            }
        }

        if (depth != 0) {
            throw new XMLStreamException(
                    "Unexpected end of XML while skipping element"
            );
        }
    }
}
