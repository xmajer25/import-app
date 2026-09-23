package com.xmajer.importapp.persistence.repository;

import com.xmajer.importapp.persistence.entity.MunicipalityPart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MunicipalityPartRepository extends JpaRepository<MunicipalityPart, String> {

    @Query("select part from MunicipalityPart part join fetch part.municipality")
    List<MunicipalityPart> findAllWithMunicipality();
}
