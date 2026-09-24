package com.xmajer.importapp.importer.service;

import com.xmajer.importapp.importer.model.source.ParsedAddressImport;
import com.xmajer.importapp.importer.model.summary.ImportSaveSummary;
import com.xmajer.importapp.importer.service.writer.MunicipalityWriter;
import com.xmajer.importapp.importer.service.writer.MunicipalityPartWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ImportPersistenceService {

    private final MunicipalityWriter municipalityWriter;
    private final MunicipalityPartWriter municipalityPartWriter;

    @Transactional
    public ImportSaveSummary save(ParsedAddressImport data) {
        var municipalityCounts = municipalityWriter.save(
                data.municipalities()
        );

        var municipalityPartCounts = municipalityPartWriter.save(
                data.municipalityParts()
        );

        return ImportSaveSummary.fromWriteCounts(
                municipalityCounts,
                municipalityPartCounts
        );
    }
}
