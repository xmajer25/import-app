package com.xmajer.importapp.persistence.repository;

import com.xmajer.importapp.persistence.entity.MunicipalityExtended;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MunicipalityExtendedRepository extends JpaRepository<MunicipalityExtended, String> {

    @Query("""
            select extended
            from MunicipalityExtended extended
            join fetch extended.municipality
            """)
    List<MunicipalityExtended> findAllWithMunicipality();
}
