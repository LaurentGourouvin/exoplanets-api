package com.example.exoplanetes.controllers;

import com.example.exoplanetes.dto.CreateObservatoireRequest;
import com.example.exoplanetes.dto.ObservatoireResponse;
import com.example.exoplanetes.entities.Observatoire;
import com.example.exoplanetes.services.ObservatoireService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/observatoires")
public class ObservatoireController {

    private final ObservatoireService observatoireService;

    public ObservatoireController(ObservatoireService observatoireService) {
        this.observatoireService = observatoireService;
    }

    @GetMapping
    public ResponseEntity<List<ObservatoireResponse>> getList() {
        List<ObservatoireResponse> observatoires = this.observatoireService.list();
        return ResponseEntity.status(HttpStatus.OK).body(observatoires);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ObservatoireResponse> getById(@PathVariable Long id) {
        ObservatoireResponse observatoire = this.observatoireService.getById(id);
        return ResponseEntity.status(HttpStatus.OK).body(observatoire);
    }

    @PostMapping
    public ResponseEntity<ObservatoireResponse> createObservatoire(@RequestBody @Valid CreateObservatoireRequest request) {
        ObservatoireResponse observatoire = this.observatoireService.create(request);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(observatoire.id())
                .toUri();

        return ResponseEntity.created(location).body(observatoire);
    }
}
