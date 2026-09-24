package com.xmajer.importapp.importer.validation;

import com.xmajer.importapp.importer.model.source.ParsedAddressImport;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Component
public class ImportDataValidator {

    public void validate(ParsedAddressImport data) {
        Objects.requireNonNull(data, "data must not be null");

        if (data.municipalities().isEmpty()) {
            throw new IllegalArgumentException(
                    "The XML contains no municipality records"
            );
        }

        Set<String> municipalityCodes = new HashSet<>();

        for (var municipality : data.municipalities()) {
            requireText(municipality.code(), "Municipality code");
            requireText(municipality.name(), "Municipality name");

            municipalityCodes.add(municipality.code());
        }

        for (var part : data.municipalityParts()) {
            requireText(part.code(), "Municipality part code");
            requireText(part.name(), "Municipality part name");
            requireText(part.municipalityCode(), "Parent municipality code");

            if (!municipalityCodes.contains(part.municipalityCode())) {
                throw new IllegalArgumentException(
                        "Part " + part.code()
                                + " references a municipality absent from the import: "
                                + part.municipalityCode()
                );
            }
        }
    }

    private void requireText(String value, String field) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(field + " must not be blank");
        }

        if (value.length() > 255) {
            throw new IllegalArgumentException(
                    field + " exceeds the database limit of 255 characters"
            );
        }
    }
}
