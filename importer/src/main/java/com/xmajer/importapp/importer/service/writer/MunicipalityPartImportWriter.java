package com.xmajer.importapp.importer.service.writer;

import com.xmajer.importapp.importer.model.MunicipalityPartData;
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
public class MunicipalityPartImportWriter {

    private final MunicipalityPartRepository partRepository;

    public void save(
            Collection<MunicipalityPartData> data,
            Map<String, Municipality> municipalities
    ) {
        Map<String, MunicipalityPartData> importedParts =
                ImportWriterMaps.lastRecordByCode(
                        data,
                        MunicipalityPartData::code
                );

        Map<String, MunicipalityPart> parts = loadMunicipalityParts(
                importedParts.keySet()
        );

        var partsToSave = new ArrayList<MunicipalityPart>();

        for (var item : importedParts.values()) {
            Municipality parent = municipalities.get(item.municipalityCode());

            if (parent == null) {
                throw new IllegalArgumentException(
                        "Missing municipality: " + item.municipalityCode()
                );
            }

            MunicipalityPart part = parts.computeIfAbsent(
                    item.code(),
                    code -> new MunicipalityPart(code, item.name(), parent)
            );

            part.rename(item.name());
            part.changeMunicipality(parent);
            partsToSave.add(part);
        }

        partRepository.saveAll(partsToSave);
    }

    private Map<String, MunicipalityPart> loadMunicipalityParts(
            Collection<String> codes
    ) {
        return ImportWriterMaps.toMap(
                partRepository.findAllById(codes),
                MunicipalityPart::getCode
        );
    }
}
