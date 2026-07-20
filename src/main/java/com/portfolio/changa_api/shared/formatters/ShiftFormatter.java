package com.portfolio.changa_api.shared.formatters;

import com.portfolio.changa_api.shared.dtos.ShiftRequest;
import org.springframework.stereotype.Component;

@Component
public class ShiftFormatter {
    public ShiftRequest format(ShiftRequest request) {
        return new ShiftRequest(
                request.description().trim().toUpperCase(),
                request.dateTime(),
                request.clientAddress().trim().toUpperCase(),
                request.clientFullName().trim().toUpperCase(),
                request.clientPhoneNumber().trim().toUpperCase(),
                request.userUsername().trim().toUpperCase()
        );
    }
}
