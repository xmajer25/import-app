package com.xmajer.importapp.api.service;

import com.xmajer.importapp.api.dto.MunicipalityExtendedResponse;
import com.xmajer.importapp.api.dto.MunicipalityResponse;
import com.xmajer.importapp.api.mapper.MunicipalityExtendedMapper;
import com.xmajer.importapp.api.mapper.MunicipalityMapper;
import com.xmajer.importapp.persistence.repository.MunicipalityExtendedRepository;
import com.xmajer.importapp.persistence.repository.MunicipalityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MunicipalityService {

    private final MunicipalityRepository municipalityRepository;
    private final MunicipalityMapper municipalityMapper;
    private final MunicipalityExtendedMapper municipalityExtendedMapper;
    private final MunicipalityExtendedRepository municipalityExtendedRepository;

    public List<MunicipalityResponse> getAll() {
        return municipalityRepository.findAll()
                .stream()
                .map(municipalityMapper::toResponse)
                .toList();
    }

    public List<MunicipalityExtendedResponse> getAllExtended(){
        return municipalityExtendedRepository.findAll()
                .stream()
                .map(municipalityExtendedMapper::toResponse)
                .toList();
    }
}
