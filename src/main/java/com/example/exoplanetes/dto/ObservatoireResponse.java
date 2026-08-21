package com.example.exoplanetes.dto;

public record ObservatoireResponse(
        Long id,
        String nom,
        String pays,
        Integer altitudeM
) {
}
