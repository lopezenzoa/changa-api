package com.portfolio.changa_api.shared.dtos;

public record UserResponse(String fullName, String address, String phoneNumber, String licenseNumber,
                           FacilityResponse facility) {
}
