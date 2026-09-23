package com.xmajer.importapp.persistence.repository;

import com.xmajer.importapp.persistence.entity.MunicipalityPart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface MunicipalityPartRepository extends JpaRepository<MunicipalityPart, UUID> {
}
