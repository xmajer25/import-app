package com.xmajer.importapp.importer.model.source;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.Instant;

public record MunicipalityExtendedSourceRecord(
        @NotNull
        MunicipalitySourceRecord municipalitySourceRecord,

        @NotBlank
        @Size(max = 255)
        String gmlId,

        @NotNull
        Integer statusCode,

        @NotBlank
        @Size(max = 255)
        String districtCode,

        @NotBlank
        @Size(max = 255)
        String pouCode,

        @NotNull
        Instant validFrom,

        @NotNull
        Long transactionId,

        @NotNull
        Long globalChangeProposalId,

        @NotBlank
        @Size(max = 255)
        String grammaticalCase2,

        @NotBlank
        @Size(max = 255)
        String grammaticalCase3,

        @NotBlank
        @Size(max = 255)
        String grammaticalCase4,

        @NotBlank
        @Size(max = 255)
        String grammaticalCase6,

        @NotBlank
        @Size(max = 255)
        String grammaticalCase7,

        @NotBlank
        @Size(max = 255)
        String nutsLau
) {
}
