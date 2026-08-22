package com.example.exoplanetes.controllers;

import com.example.exoplanetes.dto.CreateExoplaneteRequest;
import com.example.exoplanetes.dto.ExoplaneteResponse;
import com.example.exoplanetes.dto.UpdateExoplaneteDto;
import com.example.exoplanetes.entities.Exoplanete;
import com.example.exoplanetes.services.ExoplaneteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@Tag(name = "Exoplanets", description = "Manage exoplanets and their lifecycle")
@RestController
@RequestMapping("/api/exoplanetes")
public class ExoplaneteController {
    private final ExoplaneteService exoplaneteService;

    public ExoplaneteController(ExoplaneteService exoplaneteService) {
        this.exoplaneteService = exoplaneteService;
    }

    @Operation(summary = "Create an exoplanet",
            description = "Creates an exoplanet linked to an existing observatory. "
                    + "The initial status is CANDIDATE.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Exoplanet created (Location header set)"),
            @ApiResponse(responseCode = "400", description = "Invalid request (field validation failed)"),
            @ApiResponse(responseCode = "404", description = "Observatory not found"),
            @ApiResponse(responseCode = "409", description = "Designation already in use")
    })
    @PostMapping
    public ResponseEntity<ExoplaneteResponse> createExoplanete(@RequestBody @Valid CreateExoplaneteRequest request) {
        ExoplaneteResponse exoplanete = this.exoplaneteService.create(request);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(exoplanete.id())
                .toUri();

        return ResponseEntity.created(location).body(exoplanete);
    }

    @Operation(summary = "Get an exoplanet",
            description = "Returns a single exoplanet by its identifier.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Exoplanet found"),
            @ApiResponse(responseCode = "404", description = "Exoplanet not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ExoplaneteResponse> getById(@PathVariable Long id) {
        ExoplaneteResponse exoplanete = this.exoplaneteService.getById(id);
        return ResponseEntity.ok(exoplanete);
    }

    @Operation(summary = "List exoplanets",
            description = "Returns a page of exoplanets, optionally filtered by observatory. "
                    + "Pagination is controlled by the page and size parameters.")
    @ApiResponse(responseCode = "200", description = "Page of exoplanets (with pagination metadata)")
    @GetMapping
    public ResponseEntity<Page<ExoplaneteResponse>> list(@RequestParam(required = false) Long observatoireId,
                                                         @RequestParam(defaultValue = "0") int page,
                                                         @RequestParam(defaultValue = "20") int size) {
        Page<ExoplaneteResponse> exoplanetes = this.exoplaneteService.getList(observatoireId, page, size);
        return ResponseEntity.ok(exoplanetes);
    }

    @Operation(summary = "Partially update an exoplanet",
            description = "Updates only the provided fields (partial PATCH). "
                    + "Status is not changed here: use the confirm/reject endpoints.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Exoplanet updated"),
            @ApiResponse(responseCode = "400", description = "Invalid request (field validation failed)"),
            @ApiResponse(responseCode = "404", description = "Exoplanet or observatory not found"),
            @ApiResponse(responseCode = "409", description = "Designation already used by another exoplanet")
    })
    @PatchMapping("/{id}")
    public ResponseEntity<ExoplaneteResponse> update(@PathVariable Long id, @Valid @RequestBody UpdateExoplaneteDto request) {
        ExoplaneteResponse exoplaneteUpdated = this.exoplaneteService.update(id, request);
        return ResponseEntity.ok(exoplaneteUpdated);
    }

    @Operation(summary = "Delete an exoplanet",
            description = "Permanently deletes an exoplanet.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Exoplanet deleted (no content)"),
            @ApiResponse(responseCode = "404", description = "Exoplanet not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        this.exoplaneteService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Confirm an exoplanet",
            description = "Transition CANDIDATE → CONFIRMEE. Only a CANDIDATE exoplanet can be confirmed.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Exoplanet confirmed"),
            @ApiResponse(responseCode = "404", description = "Exoplanet not found"),
            @ApiResponse(responseCode = "422", description = "Illegal transition (source status is not CANDIDATE)"),
            @ApiResponse(responseCode = "409", description = "Concurrent modification conflict")
    })
    @PostMapping("/{id}/confirm")
    public ResponseEntity<ExoplaneteResponse> confirmExoplanete(@PathVariable Long id) {
        ExoplaneteResponse exoplanete = this.exoplaneteService.confirm(id);
        return ResponseEntity.ok(exoplanete);
    }

    @Operation(summary = "Reject an exoplanet",
            description = "Transition CANDIDATE → REJETEE. Only a CANDIDATE exoplanet can be rejected.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Exoplanet rejected"),
            @ApiResponse(responseCode = "404", description = "Exoplanet not found"),
            @ApiResponse(responseCode = "422", description = "Illegal transition (source status is not CANDIDATE)"),
            @ApiResponse(responseCode = "409", description = "Concurrent modification conflict")
    })
    @PostMapping("/{id}/reject")
    public ResponseEntity<ExoplaneteResponse> rejectExoplanete(@PathVariable Long id) {
        ExoplaneteResponse exoplanete = this.exoplaneteService.reject(id);
        return ResponseEntity.ok(exoplanete);
    }
}
