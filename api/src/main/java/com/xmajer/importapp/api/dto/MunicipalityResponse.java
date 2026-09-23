package com.xmajer.importapp.api.dto;

import java.time.Instant;

public record MunicipalityResponse(
        String code,
        String name,
        Instant createdAt,
        Instant modifiedAt
) {
}
