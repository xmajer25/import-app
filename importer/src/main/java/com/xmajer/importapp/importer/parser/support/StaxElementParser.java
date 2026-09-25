package com.xmajer.importapp.importer.parser.support;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

import static javax.xml.stream.XMLStreamConstants.END_ELEMENT;
import static javax.xml.stream.XMLStreamConstants.START_ELEMENT;

public final class StaxElementParser<Result> {

    private final QName elementName;
    private final String elementDescription;
    private final ResultFactory<Result> resultFactory;
    private final Map<QName, FieldReader> fieldReaders;

    public StaxElementParser(
            QName elementName,
            String elementDescription,
            ResultFactory<Result> resultFactory,
            Map<QName, FieldReader> fieldReaders
    ) {
        this.elementName = Objects.requireNonNull(elementName);
        this.elementDescription = Objects.requireNonNull(elementDescription);
        this.resultFactory = Objects.requireNonNull(resultFactory);
        this.fieldReaders = Map.copyOf(fieldReaders);
    }

    public Result parse(XMLStreamReader reader)
            throws XMLStreamException {

        Map<QName, Object> fields = new LinkedHashMap<>();

        while (reader.hasNext()) {
            int event = reader.next();

            if (event == START_ELEMENT) {
                readField(reader, fields);
            }

            if (event == END_ELEMENT
                    && elementName.equals(reader.getName())) {

                requireFields(fields);

                return resultFactory.build(fields);
            }
        }

        throw new XMLStreamException(
                "Unexpected end of " + elementDescription + " element"
        );
    }

    public static StaxElementParser<Object> child(
            QName elementName,
            String elementDescription,
            QName childName,
            FieldReader childReader
    ) {
        return new StaxElementParser<>(
                elementName,
                elementDescription,
                fields -> fields.get(childName),
                Map.of(childName, childReader)
        );
    }

    private void readField(
            XMLStreamReader reader,
            Map<QName, Object> fields
    ) throws XMLStreamException {

        QName fieldName = reader.getName();
        FieldReader fieldReader = fieldReaders.get(fieldName);

        if (fieldReader == null) {
            StaxReaderUtils.skipElement(reader);
            return;
        }

        fields.put(fieldName, fieldReader.read(reader));
    }

    private void requireFields(Map<QName, Object> fields)
            throws XMLStreamException {

        for (QName fieldName : fieldReaders.keySet()) {
            if (!fields.containsKey(fieldName)) {
                throw new XMLStreamException(
                        "Missing " + fieldName.getLocalPart()
                                + " in " + elementDescription
                );
            }
        }
    }

    @FunctionalInterface
    public interface FieldReader {

        Object read(XMLStreamReader reader)
                throws XMLStreamException;
    }

    @FunctionalInterface
    public interface ResultFactory<T> {

        T build(Map<QName, Object> fields)
                throws XMLStreamException;
    }
}
