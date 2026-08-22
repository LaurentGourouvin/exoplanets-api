package com.example.exoplanetes.dto;

import java.math.BigDecimal;

public record ExoplaneteResponse(
        Long id,
        String designation,
        BigDecimal masseTerre,
        BigDecimal distanceAl,
        String statut,
        Long observatoireId
) {
}
