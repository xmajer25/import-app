package com.xmajer.importapp.importer.parser.element;

import com.xmajer.importapp.importer.model.source.MunicipalitySourceRecord;
import com.xmajer.importapp.importer.parser.support.RuianXml;
import com.xmajer.importapp.importer.parser.support.StaxElementParser;
import com.xmajer.importapp.importer.parser.support.StaxReaderUtils;
import org.springframework.stereotype.Component;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.util.Map;

@Component
public class MunicipalityXmlParser implements ElementXmlParser<MunicipalitySourceRecord> {

    private static final StaxElementParser<MunicipalitySourceRecord> PARSER =
            new StaxElementParser<>(
                    RuianXml.MUNICIPALITY,
                    "municipality",
                    MunicipalityXmlParser::build,
                    Map.of(
                            RuianXml.MUNICIPALITY_CODE, StaxReaderUtils::readText,
                            RuianXml.MUNICIPALITY_NAME, StaxReaderUtils::readText
                    )
            );

    public MunicipalitySourceRecord parse(XMLStreamReader reader)
            throws XMLStreamException {

        return PARSER.parse(reader);
    }

    private static MunicipalitySourceRecord build(Map<QName, Object> fields) {
        return new MunicipalitySourceRecord(
                (String) fields.get(RuianXml.MUNICIPALITY_CODE),
                (String) fields.get(RuianXml.MUNICIPALITY_NAME)
        );
    }
}
