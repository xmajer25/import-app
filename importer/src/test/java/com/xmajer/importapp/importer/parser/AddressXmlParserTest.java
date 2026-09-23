package com.xmajer.importapp.importer.parser;

import com.xmajer.importapp.importer.parser.element.MunicipalityPartXmlParser;
import com.xmajer.importapp.importer.parser.element.MunicipalityXmlParser;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;

class AddressXmlParserTest {

    private final AddressXmlParser parser = new AddressXmlParser(
            new MunicipalityXmlParser(),
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

        var result = parser.parse(new ByteArrayInputStream(
                xml.getBytes(StandardCharsets.UTF_8)
        ));

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
}
