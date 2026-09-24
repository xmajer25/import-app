package com.xmajer.importapp.importer.parser.element;

import com.xmajer.importapp.importer.model.source.MunicipalityPartSourceRecord;
import com.xmajer.importapp.importer.parser.support.RuianXml;
import com.xmajer.importapp.importer.parser.support.StaxElementParser;
import org.springframework.stereotype.Component;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.util.Map;

@Component
public class MunicipalityPartXmlParser {

    private static final StaxElementParser<String> MUNICIPALITY_REFERENCE_PARSER =
            new StaxElementParser<>(
                    RuianXml.PART_MUNICIPALITY,
                    "municipality reference",
                    MunicipalityPartXmlParser::municipalityCode,
                    Map.of(
                            RuianXml.MUNICIPALITY_CODE,
                            StaxElementParser.text()
                    )
            );

    private static final Map<QName, StaxElementParser.FieldReader> FIELD_READERS =
            Map.of(
                    RuianXml.PART_CODE, StaxElementParser.text(),
                    RuianXml.PART_NAME, StaxElementParser.text(),
                    RuianXml.PART_MUNICIPALITY, MUNICIPALITY_REFERENCE_PARSER::parse
            );



    private static final StaxElementParser<MunicipalityPartSourceRecord> PARSER =
            new StaxElementParser<>(
                    RuianXml.MUNICIPALITY_PART,
                    "municipality part",
                    MunicipalityPartXmlParser::build,
                    FIELD_READERS
            );

    public MunicipalityPartSourceRecord parse(XMLStreamReader reader)
            throws XMLStreamException {

        return PARSER.parse(reader);
    }

    private static MunicipalityPartSourceRecord build(Map<QName, String> fields) {
        return new MunicipalityPartSourceRecord(
                fields.get(RuianXml.PART_CODE),
                fields.get(RuianXml.PART_NAME),
                fields.get(RuianXml.PART_MUNICIPALITY)
        );
    }

    private static String municipalityCode(Map<QName, String> fields)
            throws XMLStreamException {
        String code = fields.get(RuianXml.MUNICIPALITY_CODE);

        if (code == null) {
            throw new XMLStreamException(
                    "Municipality reference does not contain a municipality code"
            );
        }

        return code;
    }
}
