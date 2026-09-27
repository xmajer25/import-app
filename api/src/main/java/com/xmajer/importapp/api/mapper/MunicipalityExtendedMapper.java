package com.xmajer.importapp.api.mapper;

import com.xmajer.importapp.api.dto.MunicipalityExtendedResponse;
import com.xmajer.importapp.persistence.entity.MunicipalityExtended;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MunicipalityExtendedMapper {

    @Mapping(target = "name", source = "municipality.name")
    MunicipalityExtendedResponse toResponse(
            MunicipalityExtended municipalityExtended
    );
}
