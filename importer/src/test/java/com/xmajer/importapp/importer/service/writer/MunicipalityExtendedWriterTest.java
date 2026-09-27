package com.xmajer.importapp.importer.service.writer;

import com.xmajer.importapp.importer.model.source.MunicipalityExtendedSourceRecord;
import com.xmajer.importapp.importer.model.source.MunicipalitySourceRecord;
import com.xmajer.importapp.persistence.entity.Municipality;
import com.xmajer.importapp.persistence.entity.MunicipalityExtended;
import com.xmajer.importapp.persistence.repository.MunicipalityExtendedRepository;
import com.xmajer.importapp.persistence.repository.MunicipalityRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.stream.StreamSupport;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MunicipalityExtendedWriterTest {

    @Mock
    private MunicipalityRepository municipalityRepository;

    @Mock
    private MunicipalityExtendedRepository municipalityExtendedRepository;

    @Test
    void savesExtendedMunicipalityUsingNestedMunicipalityCode() {
        Municipality municipality = new Municipality("573060", "Kopidlno");

        when(municipalityRepository.findAllById(any()))
                .thenReturn(List.of(municipality));
        when(municipalityExtendedRepository.findAllById(any()))
                .thenReturn(List.of());

        var writer = new MunicipalityExtendedWriter(
                municipalityExtendedRepository,
                municipalityRepository
        );

        writer.save(List.of(extendedRecord()));

        verify(municipalityExtendedRepository).saveAll(
                org.mockito.ArgumentMatchers.argThat(saved -> {
                    assertThat(toList(saved))
                            .singleElement()
                            .satisfies(extended -> {
                                assertThat(extended.getCode())
                                        .isEqualTo("573060");
                                assertThat(extended.getMunicipality())
                                        .isSameAs(municipality);
                                assertThat(extended.getGmlId())
                                        .isEqualTo("OB.573060");
                                assertThat(extended.getStatusCode())
                                        .isEqualTo(3);
                                assertThat(extended.getDistrictCode())
                                        .isEqualTo("3604");
                            });

                    return true;
                })
        );
    }

    @Test
    void rejectsExtendedMunicipalityWhenParentMunicipalityIsMissing() {
        when(municipalityRepository.findAllById(any()))
                .thenReturn(List.of());
        when(municipalityExtendedRepository.findAllById(any()))
                .thenReturn(List.of());

        var writer = new MunicipalityExtendedWriter(
                municipalityExtendedRepository,
                municipalityRepository
        );

        assertThatThrownBy(() -> writer.save(List.of(extendedRecord())))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Missing municipality: 573060");
    }

    private MunicipalityExtendedSourceRecord extendedRecord() {
        return new MunicipalityExtendedSourceRecord(
                new MunicipalitySourceRecord("573060", "Ignored by writer"),
                "OB.573060",
                3,
                "3604",
                "2186",
                Instant.parse("2019-07-10T00:00:00Z"),
                2937100L,
                2042164L,
                "Kopidlna",
                "Kopidlnu",
                "Kopidlno",
                "Kopidlně",
                "Kopidlnem",
                "CZ0522573060"
        );
    }

    private List<MunicipalityExtended> toList(
            Iterable<MunicipalityExtended> municipalitiesExtended
    ) {
        return StreamSupport.stream(
                        municipalitiesExtended.spliterator(),
                        false
                )
                .toList();
    }
}
