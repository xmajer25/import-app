package com.xmajer.importapp.api.controller;

import com.xmajer.importapp.api.dto.MunicipalityExtendedResponse;
import com.xmajer.importapp.api.dto.MunicipalityResponse;
import com.xmajer.importapp.api.service.MunicipalityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/municipality")
@RequiredArgsConstructor
@Tag(name = "Municipalities")
public class MunicipalityController {

    private final MunicipalityService municipalityService;

    @GetMapping
    @Operation(summary = "List municipalities")
    @ApiResponse(
            responseCode = "200", description = "Municipalities returned"
    )
    public ResponseEntity<List<MunicipalityResponse>> getAll() {
        return ResponseEntity.ok(municipalityService.getAll());
    }

    @GetMapping
    @Operation(summary = "List municipalities with extended data")
    @ApiResponse(
            responseCode = "200", description = "Extended municipalities returned"
    )
    public ResponseEntity<List<MunicipalityExtendedResponse>> getAllExtended() {
        return ResponseEntity.ok(municipalityService.getAllExtended());
    }
}
