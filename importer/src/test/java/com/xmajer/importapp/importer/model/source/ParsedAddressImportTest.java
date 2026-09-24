package com.xmajer.importapp.importer.model.source;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ParsedAddressImportTest {

    private final Validator validator =
            Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    void rejectsBlankSourceRecordFields() {
        var municipality = new MunicipalitySourceRecord(" ", "Kopidlno");

        var part = new MunicipalityPartSourceRecord(
                "12345",
                "",
                "599735"
        );

        assertThat(validator.validate(municipality))
                .extracting(violation -> violation.getPropertyPath().toString())
                .contains("code");
        assertThat(validator.validate(part))
                .extracting(violation -> violation.getPropertyPath().toString())
                .contains("name");
    }

    @Test
    void rejectsSourceRecordFieldsLongerThanDatabaseLimit() {
        String tooLong = "x".repeat(256);

        var municipality = new MunicipalitySourceRecord("599735", tooLong);

        assertThat(validator.validate(municipality))
                .extracting(violation -> violation.getPropertyPath().toString())
                .contains("name");
    }

    @Test
    void rejectsImportWithoutMunicipalities() {
        var data = new ParsedAddressImport(
                List.of(),
                List.of()
        );

        assertThat(validator.validate(data))
                .extracting(violation -> violation.getPropertyPath().toString())
                .contains("municipalities");
    }

    @Test
    void cascadesValidationToSourceRecords() {
        var data = new ParsedAddressImport(
                List.of(new MunicipalitySourceRecord(" ", "Kopidlno")),
                List.of()
        );

        assertThat(validator.validate(data))
                .extracting(violation -> violation.getPropertyPath().toString())
                .contains("municipalities[0].code");
    }
}
