package com.example.exoplanetes.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateObservatoireRequest(
        @NotBlank String nom,
        @NotBlank String pays,
        @NotNull Integer altitudeM
) {
}
