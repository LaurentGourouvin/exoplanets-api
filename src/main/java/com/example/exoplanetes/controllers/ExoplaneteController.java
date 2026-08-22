package com.example.exoplanetes.controllers;

import com.example.exoplanetes.dto.CreateExoplaneteRequest;
import com.example.exoplanetes.dto.ExoplaneteResponse;
import com.example.exoplanetes.dto.UpdateExoplaneteDto;
import com.example.exoplanetes.services.ExoplaneteService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/exoplanetes")
public class ExoplaneteController {
    private final ExoplaneteService exoplaneteService;

    public ExoplaneteController(ExoplaneteService exoplaneteService) {
        this.exoplaneteService = exoplaneteService;
    }

    @PostMapping
    public ResponseEntity<ExoplaneteResponse> createExoplanete(@RequestBody @Valid CreateExoplaneteRequest request) {
        ExoplaneteResponse exoplanete = this.exoplaneteService.create(request);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(exoplanete.id())
                .toUri();

        return ResponseEntity.created(location).body(exoplanete);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExoplaneteResponse> getById(@PathVariable Long id) {
        ExoplaneteResponse exoplanete = this.exoplaneteService.getById(id);
        return ResponseEntity.ok(exoplanete);
    }

    @GetMapping
    public ResponseEntity<Page<ExoplaneteResponse>> list(@RequestParam(required = false) Long observatoireId,
                                                         @RequestParam(defaultValue = "0") int page,
                                                         @RequestParam(defaultValue = "20") int size) {
        Page<ExoplaneteResponse> exoplanetes = this.exoplaneteService.getList(observatoireId, page, size);
        return ResponseEntity.ok(exoplanetes);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ExoplaneteResponse> update(@PathVariable Long id, @Valid @RequestBody UpdateExoplaneteDto request) {
        ExoplaneteResponse exoplaneteUpdated = this.exoplaneteService.update(id, request);
        return ResponseEntity.ok(exoplaneteUpdated);
    }
}
