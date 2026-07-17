package com.portfolio.changa_api.shared.validators;

import com.portfolio.changa_api.shared.dtos.ShiftRequest;
import com.portfolio.changa_api.shared.exceptions.InvalidRequestFieldException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class ShiftValidator {
    @Autowired
    private Validator validator;

    public void validate(ShiftRequest request) throws InvalidRequestFieldException {
        Set<ConstraintViolation<ShiftRequest>> violations = validator.validate(request);

        if (!violations.isEmpty())
            throw new InvalidRequestFieldException("ANY FIELD CAN'T BE BLANK");
    }
}
