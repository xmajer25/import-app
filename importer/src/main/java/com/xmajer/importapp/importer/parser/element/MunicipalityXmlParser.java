package com.xmajer.importapp.importer.parser.element;

import com.xmajer.importapp.importer.model.source.MunicipalitySourceRecord;
import com.xmajer.importapp.importer.parser.support.RuianXml;
import com.xmajer.importapp.importer.parser.support.StaxElementParser;
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

    private static final StaxElementParser<MunicipalitySourceRecord> PARSER =
            new StaxElementParser<>(
                    RuianXml.MUNICIPALITY,
                    "municipality",
                    MunicipalityXmlParser::build,
                    FIELD_READERS
            );

    public MunicipalitySourceRecord parse(XMLStreamReader reader)
            throws XMLStreamException {

        return PARSER.parse(reader);
    }

    private static MunicipalitySourceRecord build(Map<QName, String> fields) {
        return new MunicipalitySourceRecord(
                fields.get(RuianXml.MUNICIPALITY_CODE),
                fields.get(RuianXml.MUNICIPALITY_NAME)
        );
    }
}
