package com.xmajer.importapp.api.dto;

import java.time.Instant;

public record MunicipalityPartResponse(
        String code,
        String name,
        MunicipalityResponse municipality,
        Instant createdAt,
        Instant modifiedAt
) {
}
