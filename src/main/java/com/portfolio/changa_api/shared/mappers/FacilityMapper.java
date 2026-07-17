package com.portfolio.changa_api.shared.mappers;

import com.portfolio.changa_api.model.Facility;
import com.portfolio.changa_api.shared.dtos.FacilityRequest;
import com.portfolio.changa_api.shared.dtos.FacilityResponse;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class FacilityMapper {
    public Facility toEntity(FacilityRequest request) {
        return new Facility(
                null,
                request.getName(),
                request.getDescription(),
                new ArrayList<>()
        );
    }

    public FacilityResponse toResponse(Facility entity) {
        return new FacilityResponse(
                entity.getName(),
                entity.getDescription(),
                entity.getId()
        );
    }
}
