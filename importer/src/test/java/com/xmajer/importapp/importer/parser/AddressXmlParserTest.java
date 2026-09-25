package com.xmajer.importapp.importer.parser;

import com.xmajer.importapp.importer.cli.ImportOptions;
import com.xmajer.importapp.importer.parser.element.MunicipalityExtendedXmlParser;
import com.xmajer.importapp.importer.parser.element.MunicipalityPartXmlParser;
import com.xmajer.importapp.importer.parser.element.MunicipalityXmlParser;
import org.junit.jupiter.api.Test;

import javax.xml.stream.XMLStreamException;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AddressXmlParserTest {

    private final AddressXmlParser parser = new AddressXmlParser(
            new MunicipalityXmlParser(),
            new MunicipalityExtendedXmlParser(),
            new MunicipalityPartXmlParser()
    );

    @Test
    void parsesMunicipalitiesAndMunicipalityParts() throws Exception {
        String xml = """
                <root xmlns:vf="urn:cz:isvs:ruian:schemas:VymennyFormatTypy:v1"
                      xmlns:obi="urn:cz:isvs:ruian:schemas:ObecIntTypy:v1"
                      xmlns:coi="urn:cz:isvs:ruian:schemas:CastObceIntTypy:v1">
                    <vf:Obec>
                        <obi:Kod>599735</obi:Kod>
                        <obi:Nazev>Kopidlno</obi:Nazev>
                        <obi:Ignored>ignored</obi:Ignored>
                    </vf:Obec>
                    <vf:CastObce>
                        <coi:Kod>12345</coi:Kod>
                        <coi:Nazev>Drahoraz</coi:Nazev>
                        <coi:Obec>
                            <obi:Kod>599735</obi:Kod>
                        </coi:Obec>
                    </vf:CastObce>
                </root>
                """;

        var result = parser.parse(
                new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8)),
                new ImportOptions(false)
        );

        assertThat(result.municipalities())
                .singleElement()
                .satisfies(municipality -> {
                    assertThat(municipality.code()).isEqualTo("599735");
                    assertThat(municipality.name()).isEqualTo("Kopidlno");
                });

        assertThat(result.municipalityParts())
                .singleElement()
                .satisfies(part -> {
                    assertThat(part.code()).isEqualTo("12345");
                    assertThat(part.name()).isEqualTo("Drahoraz");
                    assertThat(part.municipalityCode()).isEqualTo("599735");
                });
    }

    @Test
    void parsesExtendedMunicipalitiesWhenEnabled() throws Exception {
        String xml = extendedMunicipalityXml(
                "<obi:StatusKod>3</obi:StatusKod>"
        );

        var result = parser.parse(
                new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8)),
                new ImportOptions(true)
        );

        assertThat(result.municipalities())
                .singleElement()
                .satisfies(municipality -> {
                    assertThat(municipality.code()).isEqualTo("573060");
                    assertThat(municipality.name()).isEqualTo("Kopidlno");
                });

        assertThat(result.municipalitiesExtended())
                .singleElement()
                .satisfies(extended -> {
                    assertThat(extended.municipalitySourceRecord().code())
                            .isEqualTo("573060");
                    assertThat(extended.gmlId()).isEqualTo("OB.573060");
                    assertThat(extended.statusCode()).isEqualTo(3);
                    assertThat(extended.districtCode()).isEqualTo("3604");
                    assertThat(extended.pouCode()).isEqualTo("2186");
                    assertThat(extended.grammaticalCase2())
                            .isEqualTo("Kopidlna");
                });
    }

    @Test
    void throwsWhenRequiredExtendedMunicipalityFieldIsMissing() {
        String xml = extendedMunicipalityXml("");

        assertThatThrownBy(() -> parser.parse(
                new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8)),
                new ImportOptions(true)
        ))
                .isInstanceOf(XMLStreamException.class)
                .hasMessageContaining("Missing StatusKod in municipality");
    }

    @Test
    void throwsWhenExtendedMunicipalityNumberIsInvalid() {
        String xml = extendedMunicipalityXml(
                "<obi:StatusKod>invalid</obi:StatusKod>"
        );

        assertThatThrownBy(() -> parser.parse(
                new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8)),
                new ImportOptions(true)
        ))
                .isInstanceOf(XMLStreamException.class)
                .hasMessageContaining("Invalid integer value: invalid");
    }

    private static String extendedMunicipalityXml(String statusCode) {
        return """
                <root xmlns:vf="urn:cz:isvs:ruian:schemas:VymennyFormatTypy:v1"
                      xmlns:obi="urn:cz:isvs:ruian:schemas:ObecIntTypy:v1"
                      xmlns:oki="urn:cz:isvs:ruian:schemas:OkresIntTypy:v1"
                      xmlns:pui="urn:cz:isvs:ruian:schemas:PouIntTypy:v1"
                      xmlns:com="urn:cz:isvs:ruian:schemas:ComTypy:v1"
                      xmlns:gml="http://www.opengis.net/gml/3.2">
                    <vf:Obec gml:id="OB.573060">
                        <obi:Kod>573060</obi:Kod>
                        <obi:Nazev>Kopidlno</obi:Nazev>
                        %s
                        <obi:Okres>
                            <oki:Kod>3604</oki:Kod>
                        </obi:Okres>
                        <obi:Pou>
                            <pui:Kod>2186</pui:Kod>
                        </obi:Pou>
                        <obi:PlatiOd>2019-07-10T00:00:00</obi:PlatiOd>
                        <obi:IdTransakce>2937100</obi:IdTransakce>
                        <obi:GlobalniIdNavrhuZmeny>2042164</obi:GlobalniIdNavrhuZmeny>
                        <obi:MluvnickeCharakteristiky>
                            <com:Pad2>Kopidlna</com:Pad2>
                            <com:Pad3>Kopidlnu</com:Pad3>
                            <com:Pad4>Kopidlno</com:Pad4>
                            <com:Pad6>KopidlnÄ›</com:Pad6>
                            <com:Pad7>Kopidlnem</com:Pad7>
                        </obi:MluvnickeCharakteristiky>
                        <obi:NutsLau>CZ0522573060</obi:NutsLau>
                    </vf:Obec>
                </root>
                """.formatted(statusCode);
    }
}
