package com.xmajer.importapp.api.controller;

import com.xmajer.importapp.api.dto.MunicipalityPartResponse;
import com.xmajer.importapp.api.service.MunicipalityPartService;
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
@RequestMapping("/api/municipality_parts")
@RequiredArgsConstructor
@Tag(name = "Municipality parts")
public class MunicipalityPartController {

    private final MunicipalityPartService municipalityPartService;

    @GetMapping
    @Operation(summary = "List all municipality parts")
    @ApiResponse(
            responseCode = "200", description = "All municipality parts returned"
    )
    public ResponseEntity<List<MunicipalityPartResponse>> getAll() {
        return ResponseEntity.ok(municipalityPartService.getAll());
    }
}
