package com.portfolio.changa_api.shared.dtos;

import jakarta.validation.constraints.NotBlank;

public record UserRequest(
        @NotBlank String username,
        @NotBlank String password,
        @NotBlank String fullName,
        @NotBlank String address,
        @NotBlank String phoneNumber,
        String licenseNumber,
        FacilityRequest facility) {
}
