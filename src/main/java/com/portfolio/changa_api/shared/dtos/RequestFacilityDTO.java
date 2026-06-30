package com.portfolio.changa_api.shared.dtos;

import lombok.EqualsAndHashCode;
import lombok.Getter;


@Getter
@EqualsAndHashCode
public class RequestFacilityDTO {
    private final String name;
    private final String description;

    public RequestFacilityDTO(String name, String description) {
        this.name = name;
        this.description = description;
    }
}
