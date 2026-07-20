package com.portfolio.changa_api.shared.dtos;

import jakarta.validation.constraints.NotBlank;

public record UserResponse(
        @NotBlank String fullName,
        @NotBlank String address,
        @NotBlank String phoneNumber,
        @NotBlank String licenseNumber,
        FacilityResponse facility) {
}
