package com.xmajer.importapp.importer.model.source;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ParsedAddressImportTest {

    @Test
    void rejectsBlankSourceRecordFields() {
        assertThatThrownBy(() -> new MunicipalitySourceRecord(" ", "Kopidlno"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Municipality code must not be blank");

        assertThatThrownBy(() -> new MunicipalityPartSourceRecord(
                "12345",
                "",
                "599735"
        ))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Municipality part name must not be blank");
    }

    @Test
    void rejectsSourceRecordFieldsLongerThanDatabaseLimit() {
        String tooLong = "x".repeat(256);

        assertThatThrownBy(() -> new MunicipalitySourceRecord("599735", tooLong))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Municipality name exceeds");
    }

    @Test
    void rejectsImportWithoutMunicipalities() {
        assertThatThrownBy(() -> new ParsedAddressImport(
                List.of(),
                List.of()
        ))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("contains no municipality records");
    }

    @Test
    void rejectsMunicipalityPartWithoutImportedParent() {
        assertThatThrownBy(() -> new ParsedAddressImport(
                List.of(new MunicipalitySourceRecord("599735", "Kopidlno")),
                List.of(new MunicipalityPartSourceRecord(
                        "12345",
                        "Drahoraz",
                        "999999"
                ))
        ))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("references a municipality absent");
    }
}
