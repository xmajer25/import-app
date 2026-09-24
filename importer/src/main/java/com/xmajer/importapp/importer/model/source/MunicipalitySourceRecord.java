package com.xmajer.importapp.importer.model.source;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record MunicipalitySourceRecord(
        @NotBlank
        @Size(max = 255)
        String code,

        @NotBlank
        @Size(max = 255)
        String name
) {
}
