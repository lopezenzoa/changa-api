package com.portfolio.changa_api.shared.formatters;

import com.portfolio.changa_api.shared.dtos.UserRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserFormatter {
    @Autowired private FacilityFormatter facilityFormatter;

    public UserRequest format(UserRequest request) {
        String licenseNumber = request.licenseNumber();

        if (request.licenseNumber().isEmpty()) {
            Number randomLicenseNumber = Math.random() * 10;
            licenseNumber = randomLicenseNumber.toString();
        }

        return new UserRequest(
                request.username().trim().toUpperCase(),
                request.password().trim().toUpperCase(),
                request.fullName().trim().toUpperCase(),
                request.address().trim().toUpperCase(),
                request.phoneNumber().trim().toUpperCase(),
                licenseNumber.toUpperCase(),
                facilityFormatter.format(request.facility())
        );
    }
}
