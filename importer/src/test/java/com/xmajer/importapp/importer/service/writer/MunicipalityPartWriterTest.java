package com.xmajer.importapp.importer.service.writer;

import com.xmajer.importapp.importer.model.source.MunicipalityPartSourceRecord;
import com.xmajer.importapp.persistence.entity.Municipality;
import com.xmajer.importapp.persistence.entity.MunicipalityPart;
import com.xmajer.importapp.persistence.repository.MunicipalityPartRepository;
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
class MunicipalityPartWriterTest {

    @Mock
    private MunicipalityRepository municipalityRepository;

    @Mock
    private MunicipalityPartRepository municipalityPartRepository;

    @Test
    void updatesExistingMunicipalityPart() {
        Municipality parent = new Municipality("573060", "Kopidlno");
        Municipality oldParent = new Municipality("000001", "Old");
        MunicipalityPart existingPart = new MunicipalityPart(
                "69299",
                "Old name",
                oldParent
        );

        when(municipalityPartRepository.findAllById(any()))
                .thenReturn(List.of(existingPart));
        when(municipalityRepository.findAllById(any()))
                .thenReturn(List.of(parent));

        var writer = new MunicipalityPartWriter(
                municipalityPartRepository,
                municipalityRepository
        );

        var counts = writer.save(List.of(
                new MunicipalityPartSourceRecord(
                        "69299",
                        "Kopidlno",
                        "573060"
                )
        ));

        assertThat(counts.recordsCreated()).isZero();
        assertThat(counts.recordsUpdated()).isOne();

        verify(municipalityPartRepository).saveAll(
                org.mockito.ArgumentMatchers.argThat(saved -> {
                    assertThat(toList(saved))
                            .singleElement()
                            .satisfies(part -> {
                                assertThat(part).isSameAs(existingPart);
                                assertThat(part.getName())
                                        .isEqualTo("Kopidlno");
                                assertThat(part.getMunicipality())
                                        .isSameAs(parent);
                            });

                    return true;
                })
        );
    }

    @Test
    void rejectsMunicipalityPartWhenParentMunicipalityIsMissing() {
        when(municipalityPartRepository.findAllById(any()))
                .thenReturn(List.of());
        when(municipalityRepository.findAllById(any()))
                .thenReturn(List.of());

        var writer = new MunicipalityPartWriter(
                municipalityPartRepository,
                municipalityRepository
        );

        assertThatThrownBy(() -> writer.save(List.of(
                new MunicipalityPartSourceRecord(
                        "69299",
                        "Kopidlno",
                        "573060"
                )
        )))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Missing municipality: 573060");
    }

    private List<MunicipalityPart> toList(Iterable<MunicipalityPart> parts) {
        return StreamSupport.stream(parts.spliterator(), false)
                .toList();
    }
}
