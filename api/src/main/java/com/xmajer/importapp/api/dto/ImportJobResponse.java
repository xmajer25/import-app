package com.xmajer.importapp.api.dto;

import com.xmajer.importapp.persistence.entity.enums.ImportStatus;

import java.time.Instant;
import java.util.UUID;

public record ImportJobResponse(
        UUID id,
        Instant startedAt,
        Instant finishedAt,
        ImportStatus importStatus,
        String sourceUrl,
        int municipalitiesCreated,
        int municipalitiesUpdated,
        int municipalityPartsCreated,
        int municipalityPartsUpdated,
        String errorMessage
) {
}
