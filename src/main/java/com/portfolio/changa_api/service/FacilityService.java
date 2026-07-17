package com.portfolio.changa_api.service;

import com.portfolio.changa_api.model.Facility;
import com.portfolio.changa_api.repository.FacilityRepository;
import com.portfolio.changa_api.shared.exceptions.InvalidRequestFieldException;
import com.portfolio.changa_api.shared.dtos.FacilityRequest;
import com.portfolio.changa_api.shared.dtos.FacilityResponse;
import com.portfolio.changa_api.shared.exceptions.NotFoundException;
import com.portfolio.changa_api.shared.formatters.FacilityFormatter;
import com.portfolio.changa_api.shared.mappers.FacilityMapper;
import com.portfolio.changa_api.shared.validators.FacilityValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class FacilityService {
    @Autowired private FacilityRepository repository;
    @Autowired private FacilityMapper mapper;
    @Autowired private FacilityValidator validator;
    @Autowired private FacilityFormatter formatter;

    public FacilityResponse add(FacilityRequest request) throws InvalidRequestFieldException {
        validator.validate(request);

        /* FORMATTING REQUEST */
        request = formatter.format(request);

        /* SEARCHING BY NAME AN EXISTENT FACILITY */
        Optional<Facility> facility = repository.findByName(request.getName());

        if (facility.isEmpty()) {
            /* CREATE NEW FACILITY */
            Facility saved = repository.save(mapper.toEntity(request));

            return mapper.toResponse(saved);
        }

        /* USE FOUND FACILITY */
        return mapper.toResponse(facility.get());
    }

    public FacilityResponse getByName(String name) throws NotFoundException, InvalidRequestFieldException {
        validator.validate(name);

        /* FORMATTING PARAM */
        name = name.trim().toUpperCase();

        Optional<Facility> facility = repository.findByName(name);

        if (facility.isEmpty())
            throw new NotFoundException("FACILITY WITH NAME '" + name + "' NOT FOUND");

        return mapper.toResponse(facility.get());
    }

    public Boolean deleteByName(String name) throws InvalidRequestFieldException, NotFoundException {
        validator.validate(name);

        /* FORMATTING PARAM */
        name = name.trim().toUpperCase();

        Optional<Facility> facility = repository.findByName(name);

        if (facility.isEmpty())
            throw new NotFoundException("FACILITY WITH NAME '" + name + "' NOT FOUND");

        repository.delete(facility.get());

        return true;
    }

    /* THIS IS USED INSIDE THE PACKAGE */
    protected Facility save(FacilityRequest request) {
        validator.validate(request);

        /* FORMATTING REQUEST */
        request = formatter.format(request);

        /* SEARCHING BY NAME AN EXISTENT FACILITY */
        Optional<Facility> facility = repository.findByName(request.getName());

        if (facility.isEmpty()) {
            /* CREATE NEW FACILITY */
            return repository.save(mapper.toEntity(request));
        }

        /* USE FOUND FACILITY */
        return facility.get();
    }
}
