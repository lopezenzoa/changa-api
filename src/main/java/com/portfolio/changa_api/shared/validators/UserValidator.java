package com.portfolio.changa_api.shared.validators;

import com.portfolio.changa_api.model.User;
import com.portfolio.changa_api.repository.UserRepository;
import com.portfolio.changa_api.shared.dtos.UserRequest;
import com.portfolio.changa_api.shared.exceptions.InvalidRequestFieldException;
import com.portfolio.changa_api.shared.exceptions.UniquenessViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserValidator {
    @Autowired private UserRepository repository;

    public void validate(UserRequest request) {
        if (
                request.username().trim().isEmpty()
                        || request.password().trim().isEmpty()
                        || request.fullName().trim().isEmpty()
                        || request.address().trim().isEmpty()
                        || request.phoneNumber().trim().isEmpty()
        )
            throw new InvalidRequestFieldException("ANY FIELD CAN'T BE EMPTY");
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
