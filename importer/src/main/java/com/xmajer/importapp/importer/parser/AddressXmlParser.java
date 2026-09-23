package com.xmajer.importapp.importer.parser;

import com.xmajer.importapp.importer.model.MunicipalityData;
import com.xmajer.importapp.importer.model.MunicipalityPartData;
import com.xmajer.importapp.importer.model.ParsedAddressData;
import com.xmajer.importapp.importer.parser.element.MunicipalityPartXmlParser;
import com.xmajer.importapp.importer.parser.element.MunicipalityXmlParser;
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

    public ParsedAddressData parse(InputStream input)
            throws XMLStreamException {

        XMLStreamReader reader = createReader(input);

        var municipalities = new ArrayList<MunicipalityData>();
        var parts = new ArrayList<MunicipalityPartData>();

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

            return new ParsedAddressData(municipalities, parts);

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
