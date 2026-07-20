package com.portfolio.changa_api.shared.validators;

import com.portfolio.changa_api.shared.dtos.FacilityRequest;
import com.portfolio.changa_api.shared.exceptions.InvalidRequestFieldException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class FacilityValidator {
    @Autowired
    private Validator validator;

    public void validate(FacilityRequest request) throws InvalidRequestFieldException {
        Set<ConstraintViolation<FacilityRequest>> violations = validator.validate(request);

        if (!violations.isEmpty())
            throw new InvalidRequestFieldException("ANY FIELD CAN'T BE BLANK");
    }

    public void validate(String name) throws InvalidRequestFieldException {
        if (name.isEmpty())
            throw new InvalidRequestFieldException("NAME CAN'T BE BLANK");
    }
}
