package com.example.exoplanetes.controllers;

import com.example.exoplanetes.dto.CreateObservatoireRequest;
import com.example.exoplanetes.dto.ObservatoireResponse;
import com.example.exoplanetes.services.ObservatoireService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@Tag(name = "Observatories", description = "Manage astronomical observatories")
@RestController
@RequestMapping("/api/observatoires")
public class ObservatoireController {

    private final ObservatoireService observatoireService;

    public ObservatoireController(ObservatoireService observatoireService) {
        this.observatoireService = observatoireService;
    }

    @Operation(summary = "List observatories",
            description = "Returns all observatories.")
    @ApiResponse(responseCode = "200", description = "List of observatories")
    @GetMapping
    public ResponseEntity<List<ObservatoireResponse>> getList() {
        List<ObservatoireResponse> observatoires = this.observatoireService.list();
        return ResponseEntity.status(HttpStatus.OK).body(observatoires);
    }

    @Operation(summary = "Get an observatory",
            description = "Returns a single observatory by its identifier.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Observatory found"),
            @ApiResponse(responseCode = "404", description = "Observatory not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ObservatoireResponse> getById(@PathVariable Long id) {
        ObservatoireResponse observatoire = this.observatoireService.getById(id);
        return ResponseEntity.status(HttpStatus.OK).body(observatoire);
    }

    @Operation(summary = "Create an observatory",
            description = "Creates a new observatory. The name must be unique.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Observatory created (Location header set)"),
            @ApiResponse(responseCode = "400", description = "Invalid request (field validation failed)"),
            @ApiResponse(responseCode = "409", description = "Name already in use")
    })
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
