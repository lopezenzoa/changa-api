package com.portfolio.changa_api.shared.formatters;

import com.portfolio.changa_api.shared.dtos.FacilityRequest;
import org.springframework.stereotype.Component;

@Component
public class FacilityFormatter {
    public FacilityRequest format(FacilityRequest dto) {
        return new FacilityRequest(
                dto.getName().trim().toUpperCase(),
                dto.getDescription().trim().toUpperCase()
        );
    }
}
