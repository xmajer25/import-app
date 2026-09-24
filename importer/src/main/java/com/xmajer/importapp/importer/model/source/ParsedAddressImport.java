package com.xmajer.importapp.importer.model.source;

import java.util.List;

public record ParsedAddressImport(
        List<MunicipalitySourceRecord> municipalities,
        List<MunicipalityPartSourceRecord> municipalityParts
) {
    public ParsedAddressImport {
        municipalities = List.copyOf(municipalities);
        municipalityParts = List.copyOf(municipalityParts);
    }
}
