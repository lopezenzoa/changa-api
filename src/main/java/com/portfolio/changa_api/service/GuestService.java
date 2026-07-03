package com.portfolio.changa_api.service;

import com.portfolio.changa_api.model.Guest;
import com.portfolio.changa_api.repository.GuestRepository;
import com.portfolio.changa_api.shared.builders.GuestBuilder;
import com.portfolio.changa_api.shared.dtos.GuestDTO;
import com.portfolio.changa_api.shared.exceptions.InvalidRequestFieldException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GuestService {
    @Autowired private GuestRepository repository;

    public GuestDTO add(GuestDTO request) throws InvalidRequestFieldException {
        /* CHECK REQUEST VALIDITY */
        checkRequestValidity(request);

        /* FORMATTING REQUEST */
        request = formatRequest(request);

        /* SEARCHING BY PHONE NUMBER AN EXISTENT GUEST */
        Optional<Guest> guest = repository.findByPhoneNumber(request.phoneNumber());

        if (guest.isEmpty()) {
            /* CREATE NEW GUEST */
            Guest saved = repository.save(
                    new GuestBuilder()
                            .setId(null)
                            .setFullName(request.fullName())
                            .setPhoneNumber(request.phoneNumber())
                            .setAddress(request.address())
                            .buildEntity()
            );

            return new GuestBuilder()
                    .setFullName(saved.getFullName())
                    .setPhoneNumber(saved.getPhoneNumber())
                    .setAddress(saved.getAddress())
                    .buildDTO();
        }

        /* USE FOUND GUEST */
        return new GuestBuilder()
                .setFullName(guest.get().getFullName())
                .setPhoneNumber(guest.get().getPhoneNumber())
                .setAddress(guest.get().getAddress())
                .buildDTO();
    }

    private void checkRequestValidity(GuestDTO request) throws InvalidRequestFieldException {
        if (request.fullName() == null || request.phoneNumber() == null || request.address() == null)
            throw new InvalidRequestFieldException("ANY FIELD CAN'T BE NULL");

        if (request.fullName().isEmpty() || request.phoneNumber().isEmpty() || request.address().isEmpty())
            throw new InvalidRequestFieldException("ANY FIELD CAN'T BE BLANK");
    }

    protected GuestDTO formatRequest(GuestDTO request) {
        return new GuestBuilder()
                .setFullName(request.fullName().trim().toUpperCase())
                .setPhoneNumber(request.phoneNumber().trim().toUpperCase())
                .setAddress(request.address().trim().toUpperCase())
                .buildDTO();
    }
}
