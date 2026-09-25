package com.xmajer.importapp.importer.parser.element;

import com.xmajer.importapp.importer.model.source.MunicipalityPartSourceRecord;
import com.xmajer.importapp.importer.parser.support.RuianXml;
import com.xmajer.importapp.importer.parser.support.StaxElementParser;
import com.xmajer.importapp.importer.parser.support.StaxReaderUtils;
import org.springframework.stereotype.Component;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.util.Map;

@Component
public class MunicipalityPartXmlParser implements ElementXmlParser<MunicipalityPartSourceRecord> {

    private static final StaxElementParser<Object> MUNICIPALITY_REFERENCE_PARSER =
            StaxElementParser.child(
                    RuianXml.PART_MUNICIPALITY,
                    "municipality reference",
                    RuianXml.MUNICIPALITY_CODE,
                    StaxReaderUtils::readText
            );

    private static final StaxElementParser<MunicipalityPartSourceRecord> PARSER =
            new StaxElementParser<>(
                    RuianXml.MUNICIPALITY_PART,
                    "municipality part",
                    MunicipalityPartXmlParser::build,
                    Map.of(
                            RuianXml.PART_CODE, StaxReaderUtils::readText,
                            RuianXml.PART_NAME, StaxReaderUtils::readText,
                            RuianXml.PART_MUNICIPALITY, MUNICIPALITY_REFERENCE_PARSER::parse
                    )
            );

    public MunicipalityPartSourceRecord parse(XMLStreamReader reader)
            throws XMLStreamException {

        return PARSER.parse(reader);
    }

    private static MunicipalityPartSourceRecord build(Map<QName, Object> fields) {
        return new MunicipalityPartSourceRecord(
                (String) fields.get(RuianXml.PART_CODE),
                (String) fields.get(RuianXml.PART_NAME),
                (String) fields.get(RuianXml.PART_MUNICIPALITY)
        );
    }
}
