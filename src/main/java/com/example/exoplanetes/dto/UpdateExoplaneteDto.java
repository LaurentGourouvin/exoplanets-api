package com.example.exoplanetes.dto;

import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record UpdateExoplaneteDto(
        String designation,
        @Positive BigDecimal masseTerre,
        @Positive BigDecimal distanceAl,
        Long observatoireId
) {
}
