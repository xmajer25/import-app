package com.xmajer.importapp.importer.service;

import com.xmajer.importapp.importer.model.summary.ImportSaveSummary;
import com.xmajer.importapp.persistence.entity.ImportJob;
import com.xmajer.importapp.persistence.entity.enums.ImportStatus;
import com.xmajer.importapp.persistence.repository.ImportJobRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URI;
import java.time.Instant;

@Service
@RequiredArgsConstructor
public class ImportJobService {

    private static final int MAX_TEXT_LENGTH = 255;

    private final ImportJobRepository importJobRepository;

    @Transactional
    public void recordSuccess(
            Instant startedAt,
            URI sourceUrl,
            ImportSaveSummary saveSummary
    ) {
        importJobRepository.save(new ImportJob(
                startedAt,
                Instant.now(),
                ImportStatus.SUCCESS,
                truncate(sourceUrl.toString()),
                saveSummary.municipalitiesCreated(),
                saveSummary.municipalitiesUpdated(),
                saveSummary.municipalityPartsCreated(),
                saveSummary.municipalityPartsUpdated(),
                null
        ));
    }

    @Transactional
    public void recordFailure(
            Instant startedAt,
            URI sourceUrl,
            Exception exception
    ) {
        importJobRepository.save(new ImportJob(
                startedAt,
                Instant.now(),
                ImportStatus.FAILURE,
                truncate(sourceUrl.toString()),
                0,
                0,
                0,
                0,
                truncate(exception.getMessage())
        ));
    }

    private String truncate(String value) {
        if (value == null || value.length() <= MAX_TEXT_LENGTH) {
            return value;
        }

        return value.substring(0, MAX_TEXT_LENGTH);
    }
}
