package com.xmajer.importapp.importer.model;

import java.util.List;

public record ParsedAddressData(
        List<MunicipalityData> municipalities,
        List<MunicipalityPartData> municipalityParts
) {
    public ParsedAddressData {
        municipalities = List.copyOf(municipalities);
        municipalityParts = List.copyOf(municipalityParts);
    }
}
