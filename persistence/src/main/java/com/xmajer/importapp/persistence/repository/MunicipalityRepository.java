package com.xmajer.importapp.persistence.repository;

import com.xmajer.importapp.persistence.entity.Municipality;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface MunicipalityRepository extends JpaRepository<Municipality, UUID> {
}
