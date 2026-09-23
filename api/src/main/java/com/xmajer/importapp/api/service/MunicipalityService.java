package com.xmajer.importapp.api.service;

import com.xmajer.importapp.api.dto.MunicipalityResponse;
import com.xmajer.importapp.api.mapper.MunicipalityMapper;
import com.xmajer.importapp.persistence.repository.MunicipalityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MunicipalityService {

    private final MunicipalityRepository municipalityRepository;
    private final MunicipalityMapper municipalityMapper;

    public List<MunicipalityResponse> getAll() {
        return municipalityRepository.findAll()
                .stream()
                .map(municipalityMapper::toResponse)
                .toList();
    }
}
