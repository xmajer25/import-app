package com.xmajer.importapp.importer.parser.element;

import com.xmajer.importapp.importer.model.source.MunicipalityExtendedSourceRecord;
import com.xmajer.importapp.importer.model.source.MunicipalitySourceRecord;
import com.xmajer.importapp.importer.parser.support.RuianXml;
import com.xmajer.importapp.importer.parser.support.StaxElementParser;
import com.xmajer.importapp.importer.parser.support.StaxReaderUtils;
import org.springframework.stereotype.Component;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.time.Instant;
import java.util.Map;

@Component
public class MunicipalityExtendedXmlParser implements ElementXmlParser<MunicipalityExtendedSourceRecord> {

    private static final StaxElementParser<Object> DISTRICT_PARSER =
            StaxElementParser.child(
                    RuianXml.MUNICIPALITY_DISTRICT,
                    "district",
                    RuianXml.DISTRICT_CODE,
                    StaxReaderUtils::readText
            );

    private static final StaxElementParser<Object> POU_PARSER =
            StaxElementParser.child(
                    RuianXml.MUNICIPALITY_POU,
                    "POU",
                    RuianXml.POU_CODE,
                    StaxReaderUtils::readText
            );

    private static final StaxElementParser<GrammaticalCases> GRAMMATICAL_CASES_PARSER =
            new StaxElementParser<>(
                    RuianXml.MUNICIPALITY_GRAMMATICAL_CASES,
                    "grammatical cases",
                    MunicipalityExtendedXmlParser::grammaticalCases,
                    Map.of(
                            RuianXml.GRAMMATICAL_CASE_2,
                            StaxReaderUtils::readText,
                            RuianXml.GRAMMATICAL_CASE_3,
                            StaxReaderUtils::readText,
                            RuianXml.GRAMMATICAL_CASE_4,
                            StaxReaderUtils::readText,
                            RuianXml.GRAMMATICAL_CASE_6,
                            StaxReaderUtils::readText,
                            RuianXml.GRAMMATICAL_CASE_7,
                            StaxReaderUtils::readText
                    )
            );

    private static final StaxElementParser<Map<QName, Object>> PARSER =
            new StaxElementParser<>(
                    RuianXml.MUNICIPALITY,
                    "municipality",
                    fields -> fields,
                    Map.ofEntries(
                            Map.entry(RuianXml.MUNICIPALITY_CODE, StaxReaderUtils::readText),
                            Map.entry(RuianXml.MUNICIPALITY_NAME, StaxReaderUtils::readText),
                            Map.entry(RuianXml.MUNICIPALITY_STATUS_CODE, StaxReaderUtils::readInteger),
                            Map.entry(RuianXml.MUNICIPALITY_DISTRICT, DISTRICT_PARSER::parse),
                            Map.entry(RuianXml.MUNICIPALITY_POU, POU_PARSER::parse),
                            Map.entry(RuianXml.MUNICIPALITY_VALID_FROM, StaxReaderUtils::readInstantUtc),
                            Map.entry(RuianXml.MUNICIPALITY_TRANSACTION_ID, StaxReaderUtils::readLong),
                            Map.entry(RuianXml.MUNICIPALITY_GLOBAL_CHANGE_PROPOSAL_ID, StaxReaderUtils::readLong),
                            Map.entry(RuianXml.MUNICIPALITY_GRAMMATICAL_CASES, GRAMMATICAL_CASES_PARSER::parse),
                            Map.entry(RuianXml.MUNICIPALITY_NUTS_LAU, StaxReaderUtils::readText)
                    )
            );

    public MunicipalityExtendedSourceRecord parse(XMLStreamReader reader)
            throws XMLStreamException {

        String gmlId = reader.getAttributeValue(RuianXml.GML, "id");

        if (gmlId == null) {
            throw new XMLStreamException("Missing gml:id in municipality");
        }

        return build(gmlId, PARSER.parse(reader));
    }

    private static MunicipalityExtendedSourceRecord build(
            String gmlId,
            Map<QName, Object> fields
    ) {
        GrammaticalCases grammaticalCases = (GrammaticalCases) fields.get(
                RuianXml.MUNICIPALITY_GRAMMATICAL_CASES
        );

         return new MunicipalityExtendedSourceRecord(
                 new MunicipalitySourceRecord(
                         (String) fields.get(RuianXml.MUNICIPALITY_CODE),
                         (String) fields.get(RuianXml.MUNICIPALITY_NAME)
                 ),
                gmlId,
                (Integer) fields.get(RuianXml.MUNICIPALITY_STATUS_CODE),
                (String) fields.get(RuianXml.MUNICIPALITY_DISTRICT),
                (String) fields.get(RuianXml.MUNICIPALITY_POU),
                (Instant) fields.get(RuianXml.MUNICIPALITY_VALID_FROM),
                (Long) fields.get(RuianXml.MUNICIPALITY_TRANSACTION_ID),
                (Long) fields.get(
                        RuianXml.MUNICIPALITY_GLOBAL_CHANGE_PROPOSAL_ID
                ),
                grammaticalCases.case2(),
                grammaticalCases.case3(),
                grammaticalCases.case4(),
                grammaticalCases.case6(),
                grammaticalCases.case7(),
                (String) fields.get(RuianXml.MUNICIPALITY_NUTS_LAU)
        );
    }

    private static GrammaticalCases grammaticalCases(
            Map<QName, Object> fields
    ) {
        return new GrammaticalCases(
                (String) fields.get(RuianXml.GRAMMATICAL_CASE_2),
                (String) fields.get(RuianXml.GRAMMATICAL_CASE_3),
                (String) fields.get(RuianXml.GRAMMATICAL_CASE_4),
                (String) fields.get(RuianXml.GRAMMATICAL_CASE_6),
                (String) fields.get(RuianXml.GRAMMATICAL_CASE_7)
        );
    }

    private record GrammaticalCases(
            String case2,
            String case3,
            String case4,
            String case6,
            String case7
    ) {
    }
}
