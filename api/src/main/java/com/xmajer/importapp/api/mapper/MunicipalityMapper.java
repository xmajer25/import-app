package com.xmajer.importapp.api.mapper;

import com.xmajer.importapp.api.dto.MunicipalityResponse;
import com.xmajer.importapp.persistence.entity.Municipality;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MunicipalityMapper {
    MunicipalityResponse toResponse(Municipality municipality);
}
