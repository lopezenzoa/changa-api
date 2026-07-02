package com.portfolio.changa_api.service;

import com.portfolio.changa_api.model.Facility;
import com.portfolio.changa_api.model.User;
import com.portfolio.changa_api.repository.FacilityRepository;
import com.portfolio.changa_api.repository.UserRepository;
import com.portfolio.changa_api.shared.dtos.RequestUserDTO;
import com.portfolio.changa_api.shared.dtos.ResponseFacilityDTO;
import com.portfolio.changa_api.shared.dtos.ResponseUserDTO;
import com.portfolio.changa_api.shared.exceptions.InvalidRequestFieldException;
import com.portfolio.changa_api.shared.exceptions.NotFoundException;
import com.portfolio.changa_api.shared.exceptions.UniquenessViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    @Autowired private UserRepository repository;
    @Autowired private FacilityService facilityService;

    public ResponseUserDTO add(RequestUserDTO request) throws InvalidRequestFieldException, UniquenessViolationException {
        /* INITIAL VALIDATION */
        checkRequestValidity(request);

        /* INITIAL FORMATTING */
        request = formatRequest(request);

        /* CHECK UNIQUENESS FOR USERNAME, PHONE NUMBER AND LICENSE NUMBER */
        checkRequestUniqueness(request);

        /* ADDING THE FACILITY FIRST */
        Facility facility = facilityService.save(request.facility());

        User entity = mapToEntity(request);

        entity.setFacility(facility);

        /* SAVING THE USER THEN */
        User saved = repository.save(entity);

        return mapToResponse(saved);
    }

    public List<ResponseUserDTO> getByFacilityName(String facilityName) throws InvalidRequestFieldException, NotFoundException {
        /* INITIAL VALIDATION */
        if (facilityName.trim().isEmpty())
            throw new InvalidRequestFieldException("FACILITY NAME CAN'T BE BLANK");

        /* INITIAL FORMATTING */
        facilityName = facilityName.trim().toUpperCase();

        /* SEARCHING FOR THE FACILITY */
        facilityService.getByName(facilityName);

        /* FILTERING USERS */
        List<User> users = repository.findAll();

        List<ResponseUserDTO> userDTOList = new ArrayList<>();

        for (User user : users)
            if (user.getFacility().getName().equals(facilityName))
                userDTOList.add(mapToResponse(user));

        return userDTOList;
    }
    
    private RequestUserDTO formatRequest(RequestUserDTO request) {
        String licenseNumber = request.licenseNumber();

        if (request.licenseNumber().isEmpty()) {
            Number randomLicenseNumber = Math.random() * 10;
            licenseNumber = randomLicenseNumber.toString();
        }

        return new RequestUserDTO(
                request.username().trim().toUpperCase(),
                request.password().trim().toUpperCase(),
                request.fullName().trim().toUpperCase(),
                request.address().trim().toUpperCase(),
                request.phoneNumber().trim().toUpperCase(),
                licenseNumber.toUpperCase(),
                facilityService.formatRequest(request.facility())
        );
    }
    
    private void checkRequestValidity(RequestUserDTO request) {
        if (
                request.username().trim().isEmpty()
                || request.password().trim().isEmpty()
                || request.fullName().trim().isEmpty()
                || request.address().trim().isEmpty()
                || request.phoneNumber().trim().isEmpty()
        )
            throw new InvalidRequestFieldException("ANY FIELD CAN'T BE EMPTY");
    }

    private void checkRequestUniqueness(RequestUserDTO request) {
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

    private User mapToEntity(RequestUserDTO request) {
        return new User(
                null,
                request.username(),
                request.password(),
                request.fullName(),
                request.address(),
                request.phoneNumber(),
                request.licenseNumber(),
                facilityService.mapToEntity(request.facility()),
                new ArrayList<>()
        );
    }

    private ResponseUserDTO mapToResponse(User entity) {
        return new ResponseUserDTO(
                entity.getFullName(),
                entity.getAddress(),
                entity.getPhoneNumber(),
                entity.getLicenseNumber(),
                facilityService.mapToResponse(entity.getFacility())
        );
    }
}
