package com.xmajer.importapp.persistence.repository;

import com.xmajer.importapp.persistence.entity.ImportJob;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ImportJobRepository extends JpaRepository<ImportJob, UUID> {
}
