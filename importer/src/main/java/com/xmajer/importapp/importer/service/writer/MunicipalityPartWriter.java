package com.xmajer.importapp.importer.service.writer;

import com.xmajer.importapp.importer.model.source.MunicipalityPartSourceRecord;
import com.xmajer.importapp.importer.model.summary.WriteCounts;
import com.xmajer.importapp.persistence.entity.Municipality;
import com.xmajer.importapp.persistence.entity.MunicipalityPart;
import com.xmajer.importapp.persistence.repository.MunicipalityPartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class MunicipalityPartWriter {

    private final MunicipalityPartRepository partRepository;

    public WriteCounts save(
            Collection<MunicipalityPartSourceRecord> data,
            Map<String, Municipality> municipalities
    ) {
        Map<String, MunicipalityPartSourceRecord> importedParts =
                RecordMaps.uniqueByCode(
                        data,
                        MunicipalityPartSourceRecord::code
                );

        Map<String, MunicipalityPart> parts = loadMunicipalityParts(
                importedParts.keySet()
        );

        var partsToSave = new ArrayList<MunicipalityPart>();
        int recordsCreated = 0;
        int recordsUpdated = 0;

        for (var item : importedParts.values()) {
            Municipality parent = municipalities.get(item.municipalityCode());

            if (parent == null) {
                throw new IllegalArgumentException(
                        "Missing municipality: " + item.municipalityCode()
                );
            }

            MunicipalityPart part = parts.get(item.code());

            if (part == null) {
                part = new MunicipalityPart(item.code(), item.name(), parent);
                parts.put(item.code(), part);
                recordsCreated++;
            } else {
                part.rename(item.name());
                part.changeMunicipality(parent);
                recordsUpdated++;
            }

            partsToSave.add(part);
        }

        partRepository.saveAll(partsToSave);

        return new WriteCounts(recordsCreated, recordsUpdated);
    }

    private Map<String, MunicipalityPart> loadMunicipalityParts(
            Collection<String> codes
    ) {
        return RecordMaps.toMap(
                partRepository.findAllById(codes),
                MunicipalityPart::getCode
        );
    }
}
