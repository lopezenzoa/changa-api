package com.portfolio.changa_api.service;

import com.portfolio.changa_api.model.Facility;
import com.portfolio.changa_api.model.User;
import com.portfolio.changa_api.repository.UserRepository;
import com.portfolio.changa_api.shared.dtos.UserRequest;
import com.portfolio.changa_api.shared.dtos.UserResponse;
import com.portfolio.changa_api.shared.exceptions.InvalidRequestFieldException;
import com.portfolio.changa_api.shared.exceptions.NotFoundException;
import com.portfolio.changa_api.shared.exceptions.UniquenessViolationException;
import com.portfolio.changa_api.shared.formatters.UserFormatter;
import com.portfolio.changa_api.shared.mappers.UserMapper;
import com.portfolio.changa_api.shared.validators.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    @Autowired private UserRepository repository;
    @Autowired private FacilityService facilityService;

    @Autowired private UserMapper mapper;
    @Autowired private UserFormatter formatter;
    @Autowired private UserValidator validator;

    public UserResponse add(UserRequest request) throws InvalidRequestFieldException, UniquenessViolationException {
        /* INITIAL VALIDATION */
        validator.validate(request);

        /* INITIAL FORMATTING */
        request = formatter.format(request);

        /* CHECK UNIQUENESS FOR USERNAME, PHONE NUMBER AND LICENSE NUMBER */
        validator.validateUniqueness(request);

        /* ADDING THE FACILITY FIRST */
        Facility facility = facilityService.save(request.facility());

        User entity = mapper.toEntity(request);

        entity.setFacility(facility);

        /* SAVING THE USER THEN */
        User saved = repository.save(entity);

        return mapper.toResponse(saved);
    }

    public List<UserResponse> getByFacilityName(String facilityName) throws InvalidRequestFieldException, NotFoundException {
        /* INITIAL VALIDATION */
        if (facilityName.trim().isEmpty())
            throw new InvalidRequestFieldException("FACILITY NAME CAN'T BE BLANK");

        /* INITIAL FORMATTING */
        facilityName = facilityName.trim().toUpperCase();

        /* SEARCHING FOR THE FACILITY */
        facilityService.getByName(facilityName);

        /* FILTERING USERS */
        List<User> users = repository.findAll();

        List<UserResponse> userDTOList = new ArrayList<>();

        for (User user : users)
            if (user.getFacility().getName().equals(facilityName))
                userDTOList.add(mapper.toResponse(user));

        return userDTOList;
    }
}
