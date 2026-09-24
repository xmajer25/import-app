package com.xmajer.importapp.importer.parser;

import com.xmajer.importapp.importer.model.source.MunicipalitySourceRecord;
import com.xmajer.importapp.importer.model.source.MunicipalityPartSourceRecord;
import com.xmajer.importapp.importer.model.source.ParsedAddressImport;
import com.xmajer.importapp.importer.parser.element.MunicipalityPartXmlParser;
import com.xmajer.importapp.importer.parser.element.MunicipalityXmlParser;
import com.xmajer.importapp.importer.parser.support.RuianXml;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.xml.StaxUtils;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.InputStream;
import java.util.ArrayList;

import static javax.xml.stream.XMLStreamConstants.START_ELEMENT;

@Component
@RequiredArgsConstructor
public class AddressXmlParser {

    private final MunicipalityXmlParser municipalityParser;
    private final MunicipalityPartXmlParser municipalityPartParser;

    public ParsedAddressImport parse(InputStream input)
            throws XMLStreamException {

        XMLStreamReader reader = createReader(input);

        var municipalities = new ArrayList<MunicipalitySourceRecord>();
        var parts = new ArrayList<MunicipalityPartSourceRecord>();

        try {
            while (reader.hasNext()) {
                if (reader.next() != START_ELEMENT) {
                    continue;
                }

                QName element = reader.getName();

                if (RuianXml.MUNICIPALITY.equals(element)) {
                    municipalities.add(
                            municipalityParser.parse(reader)
                    );
                } else if (RuianXml.MUNICIPALITY_PART.equals(element)) {
                    parts.add(
                            municipalityPartParser.parse(reader)
                    );
                }
            }

            return new ParsedAddressImport(municipalities, parts);

        } finally {
            reader.close();
        }
    }

    private XMLStreamReader createReader(InputStream input)
            throws XMLStreamException {

        var factory = StaxUtils.createDefensiveInputFactory();

        return factory.createXMLStreamReader(input);
    }
}
