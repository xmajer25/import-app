package com.xmajer.importapp.importer.service;

import com.xmajer.importapp.importer.model.source.ParsedAddressImport;
import com.xmajer.importapp.importer.model.summary.ImportSaveSummary;
import com.xmajer.importapp.importer.service.writer.MunicipalityWriter;
import com.xmajer.importapp.importer.service.writer.MunicipalityPartWriter;
import com.xmajer.importapp.persistence.entity.Municipality;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ImportPersistenceService {

    private final MunicipalityWriter municipalityWriter;
    private final MunicipalityPartWriter municipalityPartWriter;

    @Transactional
    public ImportSaveSummary save(ParsedAddressImport data) {
        Map<String, Municipality> municipalities = new LinkedHashMap<>();

        var municipalityCounts = municipalityWriter.save(
                data.municipalities(),
                municipalities
        );

        var municipalityPartCounts = municipalityPartWriter.save(
                data.municipalityParts(),
                municipalities
        );

        return ImportSaveSummary.fromWriteCounts(
                municipalityCounts,
                municipalityPartCounts
        );
    }
}
