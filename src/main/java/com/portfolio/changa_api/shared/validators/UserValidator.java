package com.portfolio.changa_api.shared.validators;

import com.portfolio.changa_api.model.User;
import com.portfolio.changa_api.repository.UserRepository;
import com.portfolio.changa_api.shared.dtos.UserRequest;
import com.portfolio.changa_api.shared.exceptions.InvalidRequestFieldException;
import com.portfolio.changa_api.shared.exceptions.UniquenessViolationException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
public class UserValidator {
    @Autowired private UserRepository repository;
    @Autowired private Validator validator;

    public void validate(UserRequest request) {
        Set<ConstraintViolation<UserRequest>> violations = validator.validate(request);

        if (!violations.isEmpty())
            throw new InvalidRequestFieldException("ANY FIELD CAN'T BE BLANK");
    }

    public void validateUniqueness(UserRequest request) {
        List<User> users = repository.findAll();

        users.forEach(user -> {
            if (
                    user.getUsername().equals(request.username())
                            || user.getPassword().equals(request.phoneNumber())
                            || (
                            !user.getLicenseNumber().isEmpty()
                                    && !request.licenseNumber().isEmpty()
                                    && user.getLicenseNumber().equals(request.licenseNumber())
                    )
            )
                throw new UniquenessViolationException("USERNAME, PHONE NUMBER OR LICENSE NUMBER ALREADY IN USED");
        });
    }
}
