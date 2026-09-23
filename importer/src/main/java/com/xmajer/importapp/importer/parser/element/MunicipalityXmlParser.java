package com.xmajer.importapp.importer.parser.element;

import com.xmajer.importapp.importer.model.MunicipalityData;
import com.xmajer.importapp.importer.parser.RuianXml;
import org.springframework.stereotype.Component;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.util.Map;

@Component
public class MunicipalityXmlParser {

    private static final Map<QName, StaxElementParser.FieldReader> FIELD_READERS =
            Map.of(
                    RuianXml.MUNICIPALITY_CODE,
                    StaxElementParser.text(),
                    RuianXml.MUNICIPALITY_NAME,
                    StaxElementParser.text()
            );

    private static final StaxElementParser<MunicipalityData> PARSER =
            new StaxElementParser<>(
                    RuianXml.MUNICIPALITY,
                    "municipality",
                    MunicipalityXmlParser::build,
                    FIELD_READERS
            );

    public MunicipalityData parse(XMLStreamReader reader)
            throws XMLStreamException {

        return PARSER.parse(reader);
    }

    private static MunicipalityData build(Map<QName, String> fields) {
        return new MunicipalityData(
                fields.get(RuianXml.MUNICIPALITY_CODE),
                fields.get(RuianXml.MUNICIPALITY_NAME)
        );
    }
}
