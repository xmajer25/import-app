package com.xmajer.importapp.importer.service.writer;

import com.xmajer.importapp.importer.model.source.MunicipalitySourceRecord;
import com.xmajer.importapp.persistence.entity.Municipality;
import com.xmajer.importapp.persistence.repository.MunicipalityRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
class MunicipalityWriterTest {

    @Mock
    private MunicipalityRepository municipalityRepository;

    @Test
    void savesMunicipalitiesAndAllowsIdenticalDuplicates() {
        when(municipalityRepository.findAllById(any()))
                .thenReturn(List.of());

        var writer = new MunicipalityWriter(municipalityRepository);

        var counts = writer.save(List.of(
                new MunicipalitySourceRecord("573060", "Kopidlno"),
                new MunicipalitySourceRecord("573060", "Kopidlno")
        ));

        assertThat(counts.recordsCreated()).isOne();
        assertThat(counts.recordsUpdated()).isZero();

        verify(municipalityRepository).saveAll(
                org.mockito.ArgumentMatchers.argThat(saved -> {
                    assertThat(toList(saved))
                            .singleElement()
                            .satisfies(municipality -> {
                                assertThat(municipality.getCode())
                                        .isEqualTo("573060");
                                assertThat(municipality.getName())
                                        .isEqualTo("Kopidlno");
                            });

                    return true;
                })
        );
    }

    @Test
    void rejectsConflictingDuplicateMunicipalities() {
        var writer = new MunicipalityWriter(municipalityRepository);

        assertThatThrownBy(() -> writer.save(List.of(
                new MunicipalitySourceRecord("573060", "Kopidlno"),
                new MunicipalitySourceRecord("573060", "Other")
        )))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(
                        "Conflicting duplicate import record for code: 573060"
                );
    }

    private List<Municipality> toList(Iterable<Municipality> municipalities) {
        return StreamSupport.stream(municipalities.spliterator(), false)
                .toList();
    }
}
