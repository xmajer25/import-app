package com.xmajer.importapp.importer.service.writer;

import com.xmajer.importapp.importer.model.source.MunicipalityExtendedSourceRecord;
import com.xmajer.importapp.persistence.entity.Municipality;
import com.xmajer.importapp.persistence.entity.MunicipalityExtended;
import com.xmajer.importapp.persistence.repository.MunicipalityExtendedRepository;
import com.xmajer.importapp.persistence.repository.MunicipalityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class MunicipalityExtendedWriter {

    private final MunicipalityExtendedRepository extendedRepository;
    private final MunicipalityRepository municipalityRepository;

    public void save(Collection<MunicipalityExtendedSourceRecord> data) {
        Map<String, MunicipalityExtendedSourceRecord> importedExtended =
                RecordMaps.uniqueByCode(
                        data,
                        MunicipalityExtendedWriter::municipalityCode
                );

        Map<String, Municipality> municipalities = RecordMaps.toMap(
                municipalityRepository.findAllById(importedExtended.keySet()),
                Municipality::getCode
        );
        Map<String, MunicipalityExtended> existingExtended = RecordMaps.toMap(
                extendedRepository.findAllById(importedExtended.keySet()),
                MunicipalityExtended::getCode
        );

        var extendedToSave = new ArrayList<MunicipalityExtended>();

        for (var item : importedExtended.values()) {
            String code = municipalityCode(item);
            Municipality municipality = municipalities.get(code);

            if (municipality == null) {
                throw new IllegalArgumentException(
                        "Missing municipality: " + code
                );
            }

            MunicipalityExtended extended = existingExtended.get(code);

            if (extended == null) {
                extended = create(item, municipality);
            } else {
                update(extended, item);
            }

            extendedToSave.add(extended);
        }

        extendedRepository.saveAll(extendedToSave);
    }

    private static String municipalityCode(
            MunicipalityExtendedSourceRecord item
    ) {
        return item.municipalitySourceRecord().code();
    }

    private MunicipalityExtended create(
            MunicipalityExtendedSourceRecord item,
            Municipality municipality
    ) {
        return new MunicipalityExtended(
                municipality,
                item.gmlId(),
                item.statusCode(),
                item.districtCode(),
                item.pouCode(),
                item.validFrom(),
                item.transactionId(),
                item.globalChangeProposalId(),
                item.grammaticalCase2(),
                item.grammaticalCase3(),
                item.grammaticalCase4(),
                item.grammaticalCase6(),
                item.grammaticalCase7(),
                item.nutsLau()
        );
    }

    private void update(
            MunicipalityExtended extended,
            MunicipalityExtendedSourceRecord item
    ) {
        extended.update(
                item.gmlId(),
                item.statusCode(),
                item.districtCode(),
                item.pouCode(),
                item.validFrom(),
                item.transactionId(),
                item.globalChangeProposalId(),
                item.grammaticalCase2(),
                item.grammaticalCase3(),
                item.grammaticalCase4(),
                item.grammaticalCase6(),
                item.grammaticalCase7(),
                item.nutsLau()
        );
    }
}
