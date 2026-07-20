package com.portfolio.changa_api.shared.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record FacilityResponse(
        @NotBlank String name,
        @NotBlank String description,
        @NotNull @PositiveOrZero Long id
) {}
