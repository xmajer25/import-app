package com.xmajer.importapp.persistence.entity;

import com.xmajer.importapp.persistence.entity.enums.ImportStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

import static java.util.Objects.requireNonNull;

@Entity
@Table(name = "import_job")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ImportJob {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "started_at", nullable = false)
    private Instant startedAt;

    @Column(name = "finished_at", nullable = false)
    private Instant finishedAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "import_status", nullable = false)
    private ImportStatus importStatus;

    @Column(name = "source_url", nullable = false)
    private String sourceUrl;

    @Column(name = "municipalities_created", nullable = false)
    private int municipalitiesCreated;

    @Column(name = "municipalities_updated", nullable = false)
    private int municipalitiesUpdated;

    @Column(name = "municipality_parts_created", nullable = false)
    private int municipalityPartsCreated;

    @Column(name = "municipality_parts_updated", nullable = false)
    private int municipalityPartsUpdated;

    @Column(name = "error_message")
    private String errorMessage;

    public ImportJob(
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
        this.startedAt = requireNonNull(startedAt);
        this.finishedAt = requireNonNull(finishedAt);
        this.importStatus = requireNonNull(importStatus);
        this.sourceUrl = requireNonNull(sourceUrl);
        this.municipalitiesCreated = municipalitiesCreated;
        this.municipalitiesUpdated = municipalitiesUpdated;
        this.municipalityPartsCreated = municipalityPartsCreated;
        this.municipalityPartsUpdated = municipalityPartsUpdated;
        this.errorMessage = errorMessage;
    }
}
