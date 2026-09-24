package com.xmajer.importapp.importer.parser.support;

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
