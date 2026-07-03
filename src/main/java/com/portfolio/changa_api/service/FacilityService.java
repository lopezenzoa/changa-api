package com.portfolio.changa_api.service;

import com.portfolio.changa_api.model.Facility;
import com.portfolio.changa_api.repository.FacilityRepository;
import com.portfolio.changa_api.shared.exceptions.InvalidRequestFieldException;
import com.portfolio.changa_api.shared.dtos.RequestFacilityDTO;
import com.portfolio.changa_api.shared.dtos.ResponseFacilityDTO;
import com.portfolio.changa_api.shared.exceptions.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class FacilityService {
    @Autowired private FacilityRepository repository;

    public ResponseFacilityDTO add(RequestFacilityDTO request) throws InvalidRequestFieldException {
        checkRequestValidity(request);

        /* FORMATTING REQUEST */
        request = formatRequest(request);

        /* SEARCHING BY NAME AN EXISTENT FACILITY */
        Optional<Facility> facility = repository.findByName(request.getName());

        if (facility.isEmpty()) {
            /* CREATE NEW FACILITY */
            Facility saved = repository.save(mapToEntity(request));

            return mapToResponse(saved);
        }

        /* USE FOUND FACILITY */
        return mapToResponse(facility.get());
    }

    public ResponseFacilityDTO getByName(String name) throws NotFoundException, InvalidRequestFieldException {
        checkNameValidity(name);

        /* FORMATTING PARAM */
        name = name.trim().toUpperCase();

        Optional<Facility> facility = repository.findByName(name);

        if (facility.isEmpty())
            throw new NotFoundException("FACILITY WITH NAME '" + name + "' NOT FOUND");

        return mapToResponse(facility.get());
    }

    public Boolean deleteByName(String name) throws InvalidRequestFieldException, NotFoundException {
        checkNameValidity(name);

        /* FORMATTING PARAM */
        name = name.trim().toUpperCase();

        Optional<Facility> facility = repository.findByName(name);

        if (facility.isEmpty())
            throw new NotFoundException("FACILITY WITH NAME '" + name + "' NOT FOUND");

        repository.delete(facility.get());

        return true;
    }

    private void checkRequestValidity(RequestFacilityDTO dto) throws InvalidRequestFieldException {
        if (dto.getName() == null || dto.getDescription() == null)
            throw new InvalidRequestFieldException("NAME OR DESCRIPTION CAN'T BE NULL");

        if (dto.getName().trim().isEmpty() || dto.getDescription().trim().isEmpty())
            throw new InvalidRequestFieldException("NAME OR DESCRIPTION CAN'T BE BLANK");
    }

    private void checkNameValidity(String name) {
        if (name == null)
            throw new InvalidRequestFieldException("NAME CAN'T BE NULL");
        if (name.trim().isEmpty())
            throw new InvalidRequestFieldException("NAME CAN'T BE BLANK");
    }

    protected RequestFacilityDTO formatRequest(RequestFacilityDTO dto) {
        return new RequestFacilityDTO(
                dto.getName().trim().toUpperCase(),
                dto.getDescription().trim().toUpperCase()
        );
    }

    protected Facility mapToEntity(RequestFacilityDTO dto) {
        return new Facility(
                null,
                dto.getName(),
                dto.getDescription(),
                new ArrayList<>()
        );
    }

    protected ResponseFacilityDTO mapToResponse(Facility entity) {
        return new ResponseFacilityDTO(
                entity.getName(),
                entity.getDescription(),
                entity.getId()
        );
    }

    /* THIS IS USED INSIDE THE PACKAGE */
    protected Facility save(RequestFacilityDTO request) {
        checkRequestValidity(request);

        /* FORMATTING REQUEST */
        request = formatRequest(request);

        /* SEARCHING BY NAME AN EXISTENT FACILITY */
        Optional<Facility> facility = repository.findByName(request.getName());

        if (facility.isEmpty()) {
            /* CREATE NEW FACILITY */
            return repository.save(mapToEntity(request));
        }

        /* USE FOUND FACILITY */
        return facility.get();
    }
}
