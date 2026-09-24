package com.xmajer.importapp.importer.service.writer;

import com.xmajer.importapp.importer.model.MunicipalityData;
import com.xmajer.importapp.persistence.entity.Municipality;
import com.xmajer.importapp.persistence.repository.MunicipalityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class MunicipalityImportWriter {

    private final MunicipalityRepository municipalityRepository;

    public Map<String, Municipality> save(Collection<MunicipalityData> data) {
        Map<String, MunicipalityData> importedMunicipalities =
                ImportWriterMaps.uniqueByCode(
                        data,
                        MunicipalityData::code
                );

        Map<String, Municipality> municipalities =
                loadMunicipalities(importedMunicipalities.keySet());

        var municipalitiesToSave = new ArrayList<Municipality>();

        for (var item : importedMunicipalities.values()) {
            Municipality municipality = municipalities.computeIfAbsent(
                    item.code(),
                    code -> new Municipality(code, item.name())
            );

            municipality.rename(item.name());
            municipalitiesToSave.add(municipality);
        }

        municipalityRepository.saveAll(municipalitiesToSave);

        return municipalities;
    }

    private Map<String, Municipality> loadMunicipalities(
            Collection<String> codes
    ) {
        return ImportWriterMaps.toMap(
                municipalityRepository.findAllById(codes),
                Municipality::getCode
        );
    }
}
