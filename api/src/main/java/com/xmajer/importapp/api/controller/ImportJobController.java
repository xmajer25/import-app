package com.xmajer.importapp.api.controller;

import com.xmajer.importapp.api.dto.ImportJobResponse;
import com.xmajer.importapp.api.service.ImportJobService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/import_jobs")
@RequiredArgsConstructor
public class ImportJobController {
    private final ImportJobService importJobService;

    @GetMapping
    @Operation(summary = "List all import jobs")
    @ApiResponse(
            responseCode = "200", description = "All import jobs returned"
    )
    public ResponseEntity<List<ImportJobResponse>> getAllImportJobs() {
        return ResponseEntity.ok(importJobService.getAll());
    }
}
