package com.portfolio.changa_api.shared.dtos;

public record RequestUserDTO(String username, String password, String fullName, String address, String phoneNumber,
                             String licenseNumber, RequestFacilityDTO facility) {
}
