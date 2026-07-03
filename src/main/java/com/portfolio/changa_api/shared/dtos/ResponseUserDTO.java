package com.portfolio.changa_api.shared.dtos;

public record ResponseUserDTO(String fullName, String address, String phoneNumber, String licenseNumber,
                              ResponseFacilityDTO facility) {
}
