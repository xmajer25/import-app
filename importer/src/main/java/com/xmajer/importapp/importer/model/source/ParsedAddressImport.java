package com.xmajer.importapp.importer.model.source;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record ParsedAddressImport(
        @NotEmpty
        List<@Valid MunicipalitySourceRecord> municipalities,
        List<@Valid MunicipalityPartSourceRecord> municipalityParts
) {
}
