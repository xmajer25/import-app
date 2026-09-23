package com.xmajer.importapp.api.mapper;

import com.xmajer.importapp.api.dto.MunicipalityPartResponse;
import com.xmajer.importapp.persistence.entity.MunicipalityPart;
import org.mapstruct.Mapper;

@Mapper(
        componentModel = "spring",
        uses = MunicipalityMapper.class
)
public interface MunicipalityPartMapper {

    MunicipalityPartResponse toResponse(MunicipalityPart municipalityPart);
}
