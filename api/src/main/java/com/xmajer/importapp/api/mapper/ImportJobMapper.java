package com.xmajer.importapp.api.mapper;

import com.xmajer.importapp.api.dto.ImportJobResponse;
import com.xmajer.importapp.persistence.entity.ImportJob;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ImportJobMapper {
    ImportJobResponse toResponse(ImportJob importJob);
}
