package com.xmajer.importapp.importer.parser.element;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

public interface ElementXmlParser<T> {
    T parse(XMLStreamReader reader) throws XMLStreamException;
}