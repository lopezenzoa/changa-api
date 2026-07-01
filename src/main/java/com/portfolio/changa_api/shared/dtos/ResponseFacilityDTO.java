package com.portfolio.changa_api.shared.dtos;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class ResponseFacilityDTO extends RequestFacilityDTO {
    private final Long id;

    public ResponseFacilityDTO(String name, String description, Long id) {
        super(name, description);
        this.id = id;
    }
}
