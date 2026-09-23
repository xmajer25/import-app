package com.xmajer.importapp.persistence.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.requireNonNull;

@Entity
@Table(name = "municipality")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Municipality extends AuditedEntity {
    @Id
    @Column(name = "code", nullable = false, updatable = false)
    private String code;

    @Column(name = "name", nullable = false)
    private String name;

    @OneToMany(
            mappedBy = "municipality",
            cascade = CascadeType.REMOVE
    )
    private List<MunicipalityPart> parts = new ArrayList<>();

    public Municipality(String code, String name) {
        this.code = requireNonNull(code);
        this.name = requireNonNull(name);
    }

    public void rename(String name) {
        this.name = requireNonNull(name);
    }
}
