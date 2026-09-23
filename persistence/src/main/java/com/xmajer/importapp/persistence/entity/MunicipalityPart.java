package com.xmajer.importapp.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static java.util.Objects.requireNonNull;

@Entity
@Table(name = "municipality_part")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MunicipalityPart extends AuditedEntity {
    @Id
    @Column(name = "code", nullable = false, updatable = false)
    private String code;

    @Column(name = "name", nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "municipality_code", nullable = false)
    private Municipality municipality;


    public MunicipalityPart(
            String code,
            String name,
            Municipality municipality
    ) {
        this.code = requireNonNull(code);
        this.name = requireNonNull(name);
        changeMunicipality(municipality);
    }

    public void rename(String name) {
        this.name = requireNonNull(name);
    }

    public void changeMunicipality(Municipality newMunicipality) {
        requireNonNull(newMunicipality);

        if (hasSameMunicipalityCode(newMunicipality)) {
            return;
        }

        if (this.municipality != null) {
            this.municipality.getParts().remove(this);
        }

        this.municipality = newMunicipality;
        newMunicipality.getParts().add(this);
    }

    private boolean hasSameMunicipalityCode(Municipality newMunicipality) {
        return this.municipality != null
                && this.municipality.getCode().equals(newMunicipality.getCode());
    }
}
