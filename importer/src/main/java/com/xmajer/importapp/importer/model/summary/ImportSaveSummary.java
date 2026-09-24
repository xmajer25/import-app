package com.xmajer.importapp.importer.model.summary;

public record ImportSaveSummary(
        int municipalitiesCreated,
        int municipalitiesUpdated,
        int municipalityPartsCreated,
        int municipalityPartsUpdated
) {

    public static ImportSaveSummary fromWriteCounts(
            WriteCounts municipalityCounts,
            WriteCounts municipalityPartCounts
    ) {
        return new ImportSaveSummary(
                municipalityCounts.recordsCreated(),
                municipalityCounts.recordsUpdated(),
                municipalityPartCounts.recordsCreated(),
                municipalityPartCounts.recordsUpdated()
        );
    }
}
