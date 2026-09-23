package com.xmajer.importapp.api.service;

import com.xmajer.importapp.api.dto.MunicipalityPartResponse;
import com.xmajer.importapp.api.mapper.MunicipalityPartMapper;
import com.xmajer.importapp.persistence.repository.MunicipalityPartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MunicipalityPartService {

    private final MunicipalityPartRepository municipalityPartRepository;
    private final MunicipalityPartMapper municipalityPartMapper;

    public List<MunicipalityPartResponse> getAll() {
        return municipalityPartRepository.findAllWithMunicipality()
                .stream()
                .map(municipalityPartMapper::toResponse)
                .toList();
    }
}
