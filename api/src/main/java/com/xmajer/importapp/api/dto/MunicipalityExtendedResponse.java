package com.xmajer.importapp.api.dto;

import java.time.Instant;

public record MunicipalityExtendedResponse(
        String code,
        String name,
        String gmlId,
        Integer statusCode,
        String districtCode,
        String pouCode,
        Instant validFrom,
        Long transactionId,
        Long globalChangeProposalId,
        String grammaticalCase2,
        String grammaticalCase3,
        String grammaticalCase4,
        String grammaticalCase6,
        String grammaticalCase7,
        String nutsLau
) {
}
