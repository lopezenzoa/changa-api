package com.portfolio.changa_api.shared.dtos;

public record UserRequest(String username, String password, String fullName, String address, String phoneNumber,
                          String licenseNumber, FacilityRequest facility) {
}
