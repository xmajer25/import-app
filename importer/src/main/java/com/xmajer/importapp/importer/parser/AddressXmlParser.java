package com.xmajer.importapp.importer.parser;

import com.xmajer.importapp.importer.cli.ImportOptions;
import com.xmajer.importapp.importer.model.source.MunicipalityExtendedSourceRecord;
import com.xmajer.importapp.importer.model.source.MunicipalitySourceRecord;
import com.xmajer.importapp.importer.model.source.MunicipalityPartSourceRecord;
import com.xmajer.importapp.importer.model.source.ParsedAddressImport;
import com.xmajer.importapp.importer.parser.element.ElementXmlParser;
import com.xmajer.importapp.importer.parser.element.MunicipalityExtendedXmlParser;
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

    private final ElementXmlParser<MunicipalitySourceRecord> municipalityParser;
    private final ElementXmlParser<MunicipalityExtendedSourceRecord> municipalityExtendedParser;
    private final ElementXmlParser<MunicipalityPartSourceRecord> municipalityPartParser;

    public ParsedAddressImport parse(
            InputStream input,
            ImportOptions options
    )
            throws XMLStreamException {

        XMLStreamReader reader = createReader(input);

        var municipalities = new ArrayList<MunicipalitySourceRecord>();
        var municipalitiesExtended = new ArrayList<MunicipalityExtendedSourceRecord>();
        var municipalityParts = new ArrayList<MunicipalityPartSourceRecord>();

        try {
            while (reader.hasNext()) {
                if (reader.next() != START_ELEMENT) {
                    continue;
                }

                QName element = reader.getName();

                if (RuianXml.MUNICIPALITY.equals(element)) {
                    if (options.municipalityExtended()) {
                        var parsedElement = municipalityExtendedParser.parse(reader);
                        municipalitiesExtended.add(parsedElement);
                        municipalities.add(parsedElement.municipalitySourceRecord());
                    } else {
                        municipalities.add(municipalityParser.parse(reader));
                    }
                } else if (RuianXml.MUNICIPALITY_PART.equals(element)) {
                    municipalityParts.add(municipalityPartParser.parse(reader));
                }
            }

            return new ParsedAddressImport(
                    municipalities,
                    municipalityParts,
                    municipalitiesExtended
            );

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
