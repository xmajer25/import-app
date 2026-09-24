package com.xmajer.importapp.importer.service.writer;

import com.xmajer.importapp.importer.model.source.MunicipalitySourceRecord;
import com.xmajer.importapp.importer.model.summary.WriteCounts;
import com.xmajer.importapp.persistence.entity.Municipality;
import com.xmajer.importapp.persistence.repository.MunicipalityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class MunicipalityWriter {

    private final MunicipalityRepository municipalityRepository;

    public WriteCounts save(Collection<MunicipalitySourceRecord> data) {
        Map<String, MunicipalitySourceRecord> importedMunicipalities =
                RecordMaps.uniqueByCode(
                        data,
                        MunicipalitySourceRecord::code
        );

        Map<String, Municipality> municipalities = RecordMaps.toMap(
                municipalityRepository.findAllById(
                        importedMunicipalities.keySet()
                ),
                Municipality::getCode
        );

        var municipalitiesToSave = new ArrayList<Municipality>();
        int recordsCreated = 0;
        int recordsUpdated = 0;

        for (var item : importedMunicipalities.values()) {
            Municipality municipality = municipalities.get(item.code());

            if (municipality == null) {
                municipality = new Municipality(item.code(), item.name());
                municipalities.put(item.code(), municipality);
                recordsCreated++;
            } else {
                municipality.rename(item.name());
                recordsUpdated++;
            }

            municipalitiesToSave.add(municipality);
        }

        municipalityRepository.saveAll(municipalitiesToSave);

        return new WriteCounts(recordsCreated, recordsUpdated);
    }
}
