package com.xmajer.importapp.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

import static java.util.Objects.requireNonNull;

@Entity
@Table(name = "municipality_extended")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MunicipalityExtended extends AuditedEntity {
    @Id
    @Column(name = "code", nullable = false, updatable = false)
    private String code;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId
    @JoinColumn(name = "code", nullable = false)
    private Municipality municipality;

    @Column(name = "gml_id", nullable = false)
    private String gmlId;

    @Column(name = "status_code", nullable = false)
    private Integer statusCode;

    @Column(name = "district_code", nullable = false)
    private String districtCode;

    @Column(name = "pou_code", nullable = false)
    private String pouCode;

    @Column(name = "valid_from", nullable = false)
    private Instant validFrom;

    @Column(name = "transaction_id", nullable = false)
    private Long transactionId;

    @Column(name = "global_change_proposal_id", nullable = false)
    private Long globalChangeProposalId;

    @Column(name = "grammatical_case_2", nullable = false)
    private String grammaticalCase2;

    @Column(name = "grammatical_case_3", nullable = false)
    private String grammaticalCase3;

    @Column(name = "grammatical_case_4", nullable = false)
    private String grammaticalCase4;

    @Column(name = "grammatical_case_6", nullable = false)
    private String grammaticalCase6;

    @Column(name = "grammatical_case_7", nullable = false)
    private String grammaticalCase7;

    @Column(name = "nuts_lau", nullable = false)
    private String nutsLau;

    @Column(name = "geometry_gml_id", nullable = false)
    private String geometryGmlId;

    @Column(name = "geometry_srs_name", nullable = false)
    private String geometrySrsName;

    @Column(name = "geometry_srs_dimension", nullable = false)
    private Integer geometrySrsDimension;

    @Column(name = "geometry_point_gml_id", nullable = false)
    private String geometryPointGmlId;

    @Column(name = "geometry_position", nullable = false)
    private String geometryPosition;

    public MunicipalityExtended(
            Municipality municipality,
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
            String nutsLau,
            String geometryGmlId,
            String geometrySrsName,
            Integer geometrySrsDimension,
            String geometryPointGmlId,
            String geometryPosition
    ) {
        this.municipality = requireNonNull(municipality);
        this.code = requireNonNull(municipality.getCode());
        this.gmlId = requireNonNull(gmlId);
        this.statusCode = requireNonNull(statusCode);
        this.districtCode = requireNonNull(districtCode);
        this.pouCode = requireNonNull(pouCode);
        this.validFrom = requireNonNull(validFrom);
        this.transactionId = requireNonNull(transactionId);
        this.globalChangeProposalId = requireNonNull(globalChangeProposalId);
        this.grammaticalCase2 = requireNonNull(grammaticalCase2);
        this.grammaticalCase3 = requireNonNull(grammaticalCase3);
        this.grammaticalCase4 = requireNonNull(grammaticalCase4);
        this.grammaticalCase6 = requireNonNull(grammaticalCase6);
        this.grammaticalCase7 = requireNonNull(grammaticalCase7);
        this.nutsLau = requireNonNull(nutsLau);
        this.geometryGmlId = requireNonNull(geometryGmlId);
        this.geometrySrsName = requireNonNull(geometrySrsName);
        this.geometrySrsDimension = requireNonNull(geometrySrsDimension);
        this.geometryPointGmlId = requireNonNull(geometryPointGmlId);
        this.geometryPosition = requireNonNull(geometryPosition);
    }
}
