package com.xmajer.importapp.api.service;

import com.xmajer.importapp.api.dto.ImportJobResponse;
import com.xmajer.importapp.api.mapper.ImportJobMapper;
import com.xmajer.importapp.persistence.repository.ImportJobRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ImportJobService {
    private final ImportJobRepository importJobRepository;
    private final ImportJobMapper importJobMapper;

    public List<ImportJobResponse> getAll() {
        return importJobRepository.findAll()
                .stream()
                .map(importJobMapper::toResponse)
                .toList();
    }

}
