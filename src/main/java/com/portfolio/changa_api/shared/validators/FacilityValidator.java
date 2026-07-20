package com.portfolio.changa_api.shared.validators;

import com.portfolio.changa_api.shared.dtos.FacilityRequest;
import com.portfolio.changa_api.shared.exceptions.InvalidRequestFieldException;
import org.springframework.stereotype.Component;

@Component
public class FacilityValidator {
    public void validate(FacilityRequest dto) throws InvalidRequestFieldException {
        if (dto.name() == null || dto.description() == null)
            throw new InvalidRequestFieldException("NAME OR DESCRIPTION CAN'T BE NULL");

        if (dto.name().trim().isEmpty() || dto.description().trim().isEmpty())
            throw new InvalidRequestFieldException("NAME OR DESCRIPTION CAN'T BE BLANK");
    }

    public void validate(String name) throws InvalidRequestFieldException {
        if (name == null)
            throw new InvalidRequestFieldException("NAME CAN'T BE NULL");
        if (name.trim().isEmpty())
            throw new InvalidRequestFieldException("NAME CAN'T BE BLANK");
    }
}
