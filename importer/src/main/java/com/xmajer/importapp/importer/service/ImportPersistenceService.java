package com.xmajer.importapp.importer.service;

import com.xmajer.importapp.importer.model.ParsedAddressData;
import com.xmajer.importapp.importer.service.writer.MunicipalityImportWriter;
import com.xmajer.importapp.importer.service.writer.MunicipalityPartImportWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ImportPersistenceService {

    private final MunicipalityImportWriter municipalityWriter;
    private final MunicipalityPartImportWriter municipalityPartWriter;

    @Transactional
    public void save(ParsedAddressData data) {
        var municipalities = municipalityWriter.save(
                data.municipalities()
        );

        municipalityPartWriter.save(
                data.municipalityParts(),
                municipalities
        );
    }
}
