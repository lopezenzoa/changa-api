package com.portfolio.changa_api.shared.dtos;

import jakarta.validation.constraints.NotBlank;

public record FacilityRequest(
        @NotBlank String name,
        @NotBlank String description
) {}
