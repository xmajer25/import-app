package com.xmajer.importapp.importer.parser;

import com.xmajer.importapp.importer.cli.ImportOptions;
import com.xmajer.importapp.importer.parser.element.MunicipalityExtendedXmlParser;
import com.xmajer.importapp.importer.parser.element.MunicipalityPartXmlParser;
import com.xmajer.importapp.importer.parser.element.MunicipalityXmlParser;
import org.junit.jupiter.api.Test;

import javax.xml.stream.XMLStreamException;
import java.io.InputStream;

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
        var result = parser.parse(
                resource("xml/ruian-valid.xml"),
                new ImportOptions(false)
        );

        assertThat(result.municipalities())
                .singleElement()
                .satisfies(municipality -> {
                    assertThat(municipality.code()).isEqualTo("573060");
                    assertThat(municipality.name()).isEqualTo("Kopidlno");
                });
        assertThat(result.municipalitiesExtended()).isEmpty();

        assertThat(result.municipalityParts())
                .singleElement()
                .satisfies(part -> {
                    assertThat(part.code()).isEqualTo("69299");
                    assertThat(part.name()).isEqualTo("Kopidlno");
                    assertThat(part.municipalityCode()).isEqualTo("573060");
                });
    }

    @Test
    void parsesExtendedMunicipalitiesWhenEnabled() throws Exception {
        var result = parser.parse(
                resource("xml/ruian-valid.xml"),
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
                    assertThat(extended.validFrom().toString())
                            .isEqualTo("2019-07-10T00:00:00Z");
                    assertThat(extended.transactionId()).isEqualTo(2937100L);
                    assertThat(extended.globalChangeProposalId())
                            .isEqualTo(2042164L);
                    assertThat(extended.grammaticalCase6())
                            .isEqualTo("Kopidlně");
                    assertThat(extended.nutsLau()).isEqualTo("CZ0522573060");
                });
    }

    @Test
    void parsesElementsAndFieldsInDifferentOrder() throws Exception {
        var result = parser.parse(
                resource("xml/ruian-reordered.xml"),
                new ImportOptions(true)
        );

        assertThat(result.municipalities())
                .singleElement()
                .satisfies(municipality -> {
                    assertThat(municipality.code()).isEqualTo("573060");
                    assertThat(municipality.name()).isEqualTo("Kopidlno");
                });

        assertThat(result.municipalityParts())
                .singleElement()
                .satisfies(part -> {
                    assertThat(part.code()).isEqualTo("69302");
                    assertThat(part.name()).isEqualTo("Drahoraz");
                    assertThat(part.municipalityCode()).isEqualTo("573060");
                });

        assertThat(result.municipalitiesExtended())
                .singleElement()
                .satisfies(extended -> {
                    assertThat(extended.statusCode()).isEqualTo(3);
                    assertThat(extended.grammaticalCase2())
                            .isEqualTo("Kopidlna");
                });
    }

    @Test
    void ignoresMissingExtendedFieldsInBasicMode() throws Exception {
        var result = parser.parse(
                resource("xml/ruian-missing-status.xml"),
                new ImportOptions(false)
        );

        assertThat(result.municipalities())
                .singleElement()
                .satisfies(municipality -> {
                    assertThat(municipality.code()).isEqualTo("573060");
                    assertThat(municipality.name()).isEqualTo("Kopidlno");
                });
        assertThat(result.municipalitiesExtended()).isEmpty();
    }

    @Test
    void throwsWhenRequiredExtendedMunicipalityFieldIsMissing() {
        assertThatThrownBy(() -> parser.parse(
                resource("xml/ruian-missing-status.xml"),
                new ImportOptions(true)
        ))
                .isInstanceOf(XMLStreamException.class)
                .hasMessageContaining("Missing StatusKod in municipality");
    }

    @Test
    void throwsWhenExtendedMunicipalityNumberIsInvalid() {
        assertThatThrownBy(() -> parser.parse(
                resource("xml/ruian-invalid-status.xml"),
                new ImportOptions(true)
        ))
                .isInstanceOf(XMLStreamException.class)
                .hasMessageContaining("Invalid integer value: letters");
    }

    private InputStream resource(String path) {
        InputStream input = getClass().getClassLoader()
                .getResourceAsStream(path);

        assertThat(input)
                .as("test resource %s", path)
                .isNotNull();

        return input;
    }
}
