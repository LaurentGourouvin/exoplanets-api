package com.example.exoplanetes.controllers;

import com.example.exoplanetes.dto.CreateExoplaneteRequest;
import com.example.exoplanetes.dto.ExoplaneteResponse;
import com.example.exoplanetes.services.ExoplaneteService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

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
}
