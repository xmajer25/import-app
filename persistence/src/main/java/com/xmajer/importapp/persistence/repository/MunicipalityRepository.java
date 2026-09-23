package com.xmajer.importapp.persistence.repository;

import com.xmajer.importapp.persistence.entity.Municipality;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MunicipalityRepository extends JpaRepository<Municipality, String> {
}
