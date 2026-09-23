package com.xmajer.importapp.importer.validation;

import com.xmajer.importapp.importer.model.MunicipalityData;
import com.xmajer.importapp.importer.model.MunicipalityPartData;
import com.xmajer.importapp.importer.model.ParsedAddressData;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ImportDataValidatorTest {

    private final ImportDataValidator validator = new ImportDataValidator();

    @Test
    void allowsDuplicateCodesBecauseTheyAreUpdatesWithinOneImport() {
        var data = new ParsedAddressData(
                List.of(
                        new MunicipalityData("599735", "Old name"),
                        new MunicipalityData("599735", "Kopidlno")
                ),
                List.of(
                        new MunicipalityPartData("12345", "Old part", "599735"),
                        new MunicipalityPartData("12345", "Drahoraz", "599735")
                )
        );

        assertThatCode(() -> validator.validate(data))
                .doesNotThrowAnyException();
    }

    @Test
    void rejectsMunicipalityPartWithoutImportedParent() {
        var data = new ParsedAddressData(
                List.of(new MunicipalityData("599735", "Kopidlno")),
                List.of(new MunicipalityPartData("12345", "Drahoraz", "999999"))
        );

        assertThatThrownBy(() -> validator.validate(data))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("references a municipality absent");
    }
}
