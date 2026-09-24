package com.xmajer.importapp.importer.service.writer;

import com.xmajer.importapp.importer.model.MunicipalityData;
import com.xmajer.importapp.importer.model.MunicipalityPartData;
import com.xmajer.importapp.persistence.entity.Municipality;
import com.xmajer.importapp.persistence.entity.MunicipalityPart;
import com.xmajer.importapp.persistence.repository.MunicipalityPartRepository;
import com.xmajer.importapp.persistence.repository.MunicipalityRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.stream.StreamSupport;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings({"unchecked", "rawtypes"})
class ImportWritersTest {

    @Mock
    private MunicipalityRepository municipalityRepository;

    @Mock
    private MunicipalityPartRepository partRepository;

    @Test
    void updatesExistingRecordsAndAllowsIdenticalDuplicates() {
        Municipality existingMunicipality = new Municipality(
                "599735",
                "Old municipality"
        );
        Municipality oldParent = new Municipality("000001", "Old parent");
        MunicipalityPart existingPart = new MunicipalityPart(
                "12345",
                "Old part",
                oldParent
        );

        when(municipalityRepository.findAllById(any()))
                .thenReturn(List.of(existingMunicipality));
        when(partRepository.findAllById(any()))
                .thenReturn(List.of(existingPart));

        var municipalityWriter = new MunicipalityImportWriter(
                municipalityRepository
        );
        var partWriter = new MunicipalityPartImportWriter(partRepository);

        var municipalities = municipalityWriter.save(List.of(
                new MunicipalityData("599735", "Kopidlno"),
                new MunicipalityData("599735", "Kopidlno")
        ));

        partWriter.save(
                List.of(
                        new MunicipalityPartData(
                                "12345",
                                "Drahoraz",
                                "599735"
                        ),
                        new MunicipalityPartData(
                                "12345",
                                "Drahoraz",
                                "599735"
                        )
                ),
                municipalities
        );

        List<Municipality> savedMunicipalities = captureSavedMunicipalities();
        assertThat(savedMunicipalities)
                .singleElement()
                .satisfies(municipality -> {
                    assertThat(municipality).isSameAs(existingMunicipality);
                    assertThat(municipality.getName()).isEqualTo("Kopidlno");
                });

        List<MunicipalityPart> savedParts = captureSavedMunicipalityParts();
        assertThat(savedParts)
                .singleElement()
                .satisfies(part -> {
                    assertThat(part).isSameAs(existingPart);
                    assertThat(part.getName()).isEqualTo("Drahoraz");
                    assertThat(part.getMunicipality())
                            .isSameAs(existingMunicipality);
                });
    }

    @Test
    void rejectsConflictingDuplicates() {
        var municipalityWriter = new MunicipalityImportWriter(
                municipalityRepository
        );

        assertThatThrownBy(() -> municipalityWriter.save(List.of(
                new MunicipalityData("599735", "Kopidlno"),
                new MunicipalityData("599735", "Other name")
        )))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(
                        "Conflicting duplicate import record for code: 599735"
                );
    }

    private List<Municipality> captureSavedMunicipalities() {
        ArgumentCaptor<Iterable<Municipality>> captor =
                ArgumentCaptor.forClass(Iterable.class);

        verify(municipalityRepository).saveAll(captor.capture());

        return StreamSupport.stream(captor.getValue().spliterator(), false)
                .toList();
    }

    private List<MunicipalityPart> captureSavedMunicipalityParts() {
        ArgumentCaptor<Iterable<MunicipalityPart>> captor =
                ArgumentCaptor.forClass(Iterable.class);

        verify(partRepository).saveAll(captor.capture());

        return StreamSupport.stream(captor.getValue().spliterator(), false)
                .toList();
    }
}
